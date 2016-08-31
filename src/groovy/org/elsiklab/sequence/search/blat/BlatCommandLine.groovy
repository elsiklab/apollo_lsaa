package org.elsiklab.sequence.search.blat

import org.bbop.apollo.sequence.search.AlignmentParsingException
import org.bbop.apollo.sequence.search.SequenceSearchTool
import org.bbop.apollo.sequence.search.SequenceSearchToolException
import org.bbop.apollo.BlastAlignment
import org.bbop.apollo.TabDelimitedAlignment
import java.nio.file.Files
import java.nio.file.Path
import org.codehaus.groovy.grails.web.json.JSONObject

class BlatCommandLine extends SequenceSearchTool {

    private String blatBin
    private String database
    private String blatUserOptions
    private String tmpDir
    private String outputDir
    private String gffFormatter
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
        gffFormatter = config.gff_exe
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
        String databaseArg = database
        String outputArg = dir.absolutePath + '/results.tab'
        String outputPsl = dir.absolutePath + '/results.psl'
        String outputGff = dir.absolutePath + '/results.gff'

        String command = blatBin + ' '
        for (String option : blatOptions) {
            command += option + ' '
        }

        log.debug "Running: ${command} ${databaseArg} ${queryArg} ${outputArg} -out=blast8"
        ("${command} ${databaseArg} ${queryArg} ${outputArg} -out=blast8").execute().waitForProcessOutput(System.out, System.err)
        log.debug "Running: ${command} ${databaseArg} ${queryArg} ${outputPsl}"
        ("${command} ${databaseArg} ${queryArg} ${outputPsl}").execute().waitForProcessOutput(System.out, System.err)
        log.debug "Running: ${gffFormatter} -f psl  -m -ver 3 -t hit -i ${outputPsl}"
        def gffContent = ("${gffFormatter} -f psl  -m -ver 3 -t hit -i ${outputPsl}").execute().text
        log.debug "Result: ${gffContent}"
        new File(outputGff).withWriterAppend('UTF-8') { it.write(gffContent) }
        ['flatfile-to-json.pl', '--config', $/{"glyph":"JBrowse/View/FeatureGlyph/Box"}/$,'--clientConfig',$/{"color":"function(feature){return(feature.get('strand')==-1?'blue':'red');}"}/$, '--trackType', 'JBrowse/View/Track/CanvasFeatures', '--trackLabel', "${dir.name}", '--gff', "${outputGff}", '--out', "${outputDir}"].execute().waitForProcessOutput(System.out, System.err)

        def timer = new Timer()
        def outputPath = outputDir // copy for timer
        def task = timer.runAfter(120 * 1000) {
            ("remove-track.pl --trackLabel ${dir.name} --out ${outputPath} --delete").execute().waitForProcessOutput(System.out, System.err)
        }

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
        String queryFileName = dir.absolutePath + '/query.fa'
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(queryFileName)))
        out.println('>query')
        out.println(query)
        out.close()
        return queryFileName
    }
}
