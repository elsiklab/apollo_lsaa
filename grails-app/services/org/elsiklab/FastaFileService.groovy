package apollo.lsaa

import grails.transaction.Transactional
import htsjdk.samtools.reference.IndexedFastaSequenceFile
import org.biojava.nbio.core.sequence.DNASequence

@Transactional
class FastaFileService {

    def serviceMethod() {
    }

    def readIndexedFasta(String file, String contig) {
        def indexedFasta = new IndexedFastaSequenceFile(new File(file))

        def ret = indexedFasta.getSequence(contig)
        return new String(ret.getBases()).trim()
        return [forward: str, reverse: new DNASequence(str).getReverseComplement().getSequenceAsString()]
    }

    def readIndexedFastaRegion(String file, String contig, Integer start, Integer end) {
        def indexedFasta = new IndexedFastaSequenceFile(new File(file))

        def ret = indexedFasta.getSubsequenceAt('scf1117875582023', 0, 100)
        def str = new String(ret.getBases()).trim()
        log.debug str
        return [forward: str, reverse: new DNASequence(str).getReverseComplement().getSequenceAsString()]
    }

    def readIndexedFastaReverse(String file, String contig) {
        def str = readIndexedFasta(file, contig)
        return new DNASequence(str).getReverseComplement().getSequenceAsString()
    }

    def readIndexedFastaRegionReverse(String file, String contig, Integer start, Integer end) {
        def str = readIndexedFastaRegion(file, contig, start, end)
        return new DNASequence(str).getReverseComplement().getSequenceAsString()
    }
}
