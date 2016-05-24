package org.elsiklab.sequence.search.blast;

import org.bbop.apollo.sequence.search.AlignmentParsingException;
import org.bbop.apollo.sequence.search.SequenceSearchTool;
import org.bbop.apollo.sequence.search.SequenceSearchToolException;
import org.bbop.apollo.TabDelimitedAlignment;
import org.bbop.apollo.BlastAlignment;
import java.nio.file.Files;
import java.nio.file.Path;
import org.codehaus.groovy.grails.web.json.JSONObject;

public class BlastCommandLine extends SequenceSearchTool {

    private String blastBin;
    private String blastFormatter;
    private String gffFormatter;
    private String database;
    private String blastUserOptions;
    private String tmpDir;
    private String outputDir;
    private boolean removeTmpDir;
    protected String [] blastOptions;
    
    @Override
    public void parseConfiguration(JSONObject config) throws SequenceSearchToolException {
        blastBin = config.search_exe
        blastFormatter = config.formatter_exe
        gffFormatter = config.gff_exe
        outputDir = config.output_dir
        database = config.database
        blastUserOptions = config.params
        removeTmpDir = config.removeTmpDir
        tmpDir = config.tmp_dir
    }


    @Override
    public Collection<BlastAlignment> search(String uniqueToken, String query, String databaseId, StringBuilder t) throws SequenceSearchToolException {
        Path p = tmpDir?
            Files.createTempDirectory(new File(tmpDir).toPath(),"blast_tmp") :
            Files.createTempDirectory("blast_tmp");
        File dir = p.toFile();

        Collection<BlastAlignment> results = runSearch(dir, query, databaseId);
    
        if (removeTmpDir && dir!=null) {
            deleteTmpDir(dir);
        }
        t.append(dir.name)

        return results
    }
    
    private Collection<BlastAlignment> runSearch(File dir, String query, String databaseId)
            throws IOException, AlignmentParsingException, InterruptedException {
        String queryArg = createQueryFasta(dir, query);
        String d = dir.getAbsolutePath();
        String arc = d + "/result.asn1";
        String gff = d + "/result.gff3";
        String tab = d + "/result.tab";
        String xml = d + "/result.xml";
        String config = "{\"glyph\":\"JBrowse/View/FeatureGlyph/ProcessedTranscript\",\"transcriptType\":\"match\",\"subParts\":\"blastn\"}"

        (blastBin+" -db " +database+ " -query "+queryArg+" -out "+arc+" -outfmt 11 "+blastUserOptions).execute().waitForProcessOutput(System.out,System.err)
        (blastFormatter+" -outfmt 6 -archive " + arc + " -out " + tab).execute().waitForProcessOutput(System.out,System.err)
        (blastFormatter+" -outfmt 0 -archive " + arc + " -out " + xml).execute().waitForProcessOutput(System.out,System.err)
        (gffFormatter+" --input "+xml+" --method blastn --match --addid --version 3 --type hit -o "+gff).execute().waitForProcessOutput(System.out,System.err)
        ("flatfile-to-json.pl --config "+config+" --trackType CanvasFeatures --trackLabel "+dir.name+" --gff "+gff+" --out "+outputDir).execute().waitForProcessOutput(System.out,System.err)


        Collection<BlastAlignment> matches = new ArrayList<BlastAlignment>();
        new File(tab).eachLine { line ->
            matches.add(new TabDelimitedAlignment(line))
        }
        return matches;
    }
    
    private void deleteTmpDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        for (File f : dir.listFiles()) {
            f.delete();
        }
        dir.delete();
    }


    private String createQueryFasta(File dir, String query) throws IOException {
        String queryFileName = dir.getAbsolutePath() + "/query.fa";
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(queryFileName)));
        out.println(">query");
        out.println(query);
        out.close();
        return queryFileName;
    }
    
}
