package org.elsiklab.sequence.search.blat

import org.bbop.apollo.sequence.search.AlignmentParsingException
import org.bbop.apollo.sequence.search.SequenceSearchTool
import org.bbop.apollo.sequence.search.SequenceSearchToolException
import org.bbop.apollo.BlastAlignment
import org.bbop.apollo.TabDelimitedAlignment
import java.nio.file.Files
import java.nio.file.Path
import org.codehaus.groovy.grails.web.json.JSONObject

public class BlatCommandLine extends SequenceSearchTool {

    private String blatBin
    private String database
    private String blatUserOptions
    private String tmpDir
    private String outputDir
    private boolean removeTmpDir
    protected String [] blatOptions

    @Override
    void parseConfiguration(JSONObject config) throws SequenceSearchToolException {
        blatBin = config.search_exe
        database = config.database
        blatUserOptions = config.params
        outputDir = config.output_dir
        removeTmpDir = config.removeTmpDir
        tmpDir = config.tmp_dir
    }

    @Override
    Collection<BlastAlignment> search(String uniqueToken, String query, String databaseId, StringBuilder t) throws SequenceSearchToolException {
        Path p = tmpDir ?
            Files.createTempDirectory(new File(tmpDir).toPath(),'blat_tmp') :
            Files.createTempDirectory('blat_tmp')
        File dir = p.toFile()

        Collection<BlastAlignment> results = runSearch(dir, query, databaseId)

        if (removeTmpDir && dir!=null) {
            deleteTmpDir(dir)
        }
        t.append(dir.name)
        return results
    }

    private Collection<BlastAlignment> runSearch(File dir, String query, String databaseId)
            throws IOException, AlignmentParsingException, InterruptedException {
        String queryArg = createQueryFasta(dir, query)
        String databaseArg = database + (databaseId != '' ? (':' + databaseId):'')
        String outputArg = dir.getAbsolutePath() + '/results.tab'
        String outputPsl = dir.getAbsolutePath() + '/results.psl'
        String outputGff = dir.getAbsolutePath() + '/results.gff'
        String config = """
        {
            "glyph": "JBrowse/View/FeatureGlyph/Box",
            "style": {
                "color":"function(feature){return(feature.get('strand')==-1?'blue':'red');}"
            }
        }
        """

        String command = blatBin + ' '
        for (String option : blatOptions) {
            command += option + ' '
        }
        ("${command} ${databaseArg} ${queryArg} ${outputArg} -out=blast8").execute().waitForProcessOutput(System.out, System.err)
        ("${command} ${databaseArg} ${queryArg} ${outputPsl}").execute().waitForProcessOutput(System.out, System.err)
        def gffContent = ("blat2gff.pl ${outputPsl}").execute().text
        new File(outputGff).withWriterAppend('UTF-8') { it.write(gffContent) }
        ("flatfile-to-json.pl --config ${config} --trackType CanvasFeatures --trackLabel ${dir.name} --gff ${outputGff} --out ${outputDir}").execute().waitForProcessOutput(System.out, System.err)

        Collection<BlastAlignment> matches = new ArrayList<BlastAlignment>()
        new File(outputArg).eachLine { line ->
            matches.add(new TabDelimitedAlignment(line))
        }

        return matches
    }

    private void deleteTmpDir(File dir) {
        if (!dir.exists()) {
            return
        }
        for (File f : dir.listFiles()) {
            f.delete()
        }
        dir.delete()
    }

    private String createQueryFasta(File dir, String query) throws IOException {
        String queryFileName = dir.getAbsolutePath() + '/query.fa'
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(queryFileName)))
        out.println('>query')
        out.println(query)
        out.close()
        return queryFileName
    }
}
