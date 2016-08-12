package org.elsiklab

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
    }

    def readIndexedFastaRegion(String file, String contig, Integer start, Integer end) {
        def indexedFasta = new IndexedFastaSequenceFile(new File(file))
        def ret = indexedFasta.getSubsequenceAt(contig, start, end)
        return new String(ret.getBases()).trim()
    }

    def readIndexedFastaReverse(String file, String contig) {
        def str = this.readIndexedFasta(file, contig)
        return new DNASequence(str).getReverseComplement().getSequenceAsString()
    }

    def readIndexedFastaRegionReverse(String file, String contig, Integer start, Integer end) {
        def str = this.readIndexedFastaRegion(file, contig, start, end)
        return new DNASequence(str).getReverseComplement().getSequenceAsString()
    }

    def readSequence(String file, String contig, Integer start, Integer end, Boolean reverse) {
        return reverse ? this.readIndexedFastaRegionReverse(file, contig, start, end) : this.readIndexedFastaRegion(file, contig, start, end)
    }
}
