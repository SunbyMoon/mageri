/*
 * Copyright (c) 2014-2015, Bolotin Dmitry, Chudakov Dmitry, Shugay Mikhail
 * (here and after addressed as Inventors)
 * All Rights Reserved
 *
 * Permission to use, copy, modify and distribute any part of this program for
 * educational, research and non-profit purposes, by non-profit institutions
 * only, without fee, and without a written agreement is hereby granted,
 * provided that the above copyright notice, this paragraph and the following
 * three paragraphs appear in all copies.
 *
 * Those desiring to incorporate this work into commercial products or use for
 * commercial purposes should contact the Inventors using one of the following
 * email addresses: chudakovdm@mail.ru, chudakovdm@gmail.com
 *
 * IN NO EVENT SHALL THE INVENTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
 * SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
 * ARISING OUT OF THE USE OF THIS SOFTWARE, EVEN IF THE INVENTORS HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * THE SOFTWARE PROVIDED HEREIN IS ON AN "AS IS" BASIS, AND THE INVENTORS HAS
 * NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS. THE INVENTORS MAKES NO REPRESENTATIONS AND EXTENDS NO
 * WARRANTIES OF ANY KIND, EITHER IMPLIED OR EXPRESS, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY OR FITNESS FOR A
 * PARTICULAR PURPOSE, OR THAT THE USE OF THE SOFTWARE WILL NOT INFRINGE ANY
 * PATENT, TRADEMARK OR OTHER RIGHTS.
 */

package com.milaboratory.mageri.core.mapping;

import com.milaboratory.core.sequence.NucleotideSQPair;
import com.milaboratory.core.sequence.nucleotide.NucleotideSequence;
import com.milaboratory.mageri.core.mapping.alignment.AlignmentResult;
import com.milaboratory.mageri.core.mutations.MutationArray;
import com.milaboratory.mageri.pipeline.analysis.Sample;

public class SAlignedConsensus extends AlignedConsensus {
    private final MutationArray mutations;
    private final AlignmentResult alignmentResult;
    private final NucleotideSQPair consensusSQPair;

    public SAlignedConsensus(Sample sample, NucleotideSequence umi,
                             NucleotideSQPair consensusSQPair,
                             AlignmentResult alignmentResult,
                             MutationArray mutations) {
        super(sample, umi);
        this.consensusSQPair = consensusSQPair;
        this.mutations = mutations;
        this.alignmentResult = alignmentResult;
    }

    public MutationArray getMutations() {
        return mutations;
    }

    public AlignmentResult getAlignmentResult() {
        return alignmentResult;
    }

    public NucleotideSQPair getConsensusSQPair() {
        return consensusSQPair;
    }

    @Override
    public boolean isMapped() {
        return alignmentResult != null;
    }

    @Override
    public boolean isAligned() {
        return isMapped() && alignmentResult.isGood();
    }

    @Override
    public boolean isChimeric() {
        return false;
    }

    @Override
    public boolean isPairedEnd() {
        return false;
    }
}