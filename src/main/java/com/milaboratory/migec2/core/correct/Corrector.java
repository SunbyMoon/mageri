/*
 * Copyright 2014 Mikhail Shugay (mikhail.shugay@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.migec2.core.correct;

import com.milaboratory.core.sequence.Range;
import com.milaboratory.migec2.core.align.reference.Reference;
import com.milaboratory.migec2.core.consalign.entity.AlignedConsensus;
import com.milaboratory.migec2.core.consalign.entity.AlignerReferenceLibrary;
import com.milaboratory.migec2.core.mutations.MigecMutation;
import com.milaboratory.migec2.core.mutations.MigecMutationsCollection;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class Corrector {
    private final AtomicInteger goodConsensuses = new AtomicInteger(),
            totalConsensuses = new AtomicInteger();
    private final CorrectorReferenceLibrary correctorReferenceLibrary;


    public Corrector(AlignerReferenceLibrary referenceLibraryWithStatistics) {
        this(referenceLibraryWithStatistics, CorrectorParameters.DEFAULT);
    }

    public Corrector(AlignerReferenceLibrary referenceLibraryWithStatistics, CorrectorParameters parameters) {
        this.correctorReferenceLibrary = new CorrectorReferenceLibrary(referenceLibraryWithStatistics, parameters);
    }

    public CorrectedConsensus correct(AlignedConsensus alignedConsensus) {
        totalConsensuses.incrementAndGet();

        Set<Integer> coverageMask = new HashSet<>();

        int offset = 0;
        for (int i = 0; i < alignedConsensus.getNumberOfReferences(); i++) {
            Reference reference = alignedConsensus.getReference(i);

            if (!reference.isDeNovo()) {
                MigecMutationsCollection mutations = alignedConsensus.getMajorMutations(i);
                MutationFilter mutationFilter = correctorReferenceLibrary.getMutationFilter(reference);
                Range range = alignedConsensus.getRange(i);

                if (!mutationFilter.good())
                    return null; // badly covered consensus

                // Update coverage mask
                for (int k = 0; k < reference.getSequence().size(); k++)
                    if (!mutationFilter.passedFilter(k))
                        coverageMask.add(k + offset);

                // Filter substitutions and indels
                int mustHaveMutationsCount = 0;
                for (MigecMutation mutation : mutations) {
                    // Check if that substitution passes coverage-quality filter 2nd step MIGEC
                    if (mutation.isSubstitution()) {
                        if (mutationFilter.hasMutation(mutation.pos(), mutation.to())) {
                            if (!mutationFilter.hasReference(mutation.pos()))
                                mustHaveMutationsCount++; // covered a hole in reference with substitution
                        } else {
                            mutation.filter();
                        }
                    } else if (!mutationFilter.hasIndel(mutation.code())) {
                        mutation.filter();
                    } else if (mutation.isDeletion()) {
                        if (!mutationFilter.hasReference(mutation.pos()))
                            mustHaveMutationsCount++; // covered a hole in reference with deletion

                        coverageMask.remove(offset + mutation.pos()); // no need to mask here
                    }
                }

                // Check if we've covered all holes in the reference, discard otherwise
                if (mustHaveMutationsCount < mutationFilter.getMustHaveMutationsCount())
                    return null;

                // Shift coverage mask
                offset += range.length();
            }
        }

        goodConsensuses.incrementAndGet();

        return new CorrectedConsensus(alignedConsensus, coverageMask);
    }

    public CorrectorReferenceLibrary getCorrectorReferenceLibrary() {
        return correctorReferenceLibrary;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("#").append(super.toString()).append('\n').
                append("Total consensuses: ").append(totalConsensuses.get()).
                append("Corrected consensuses: ").append(goodConsensuses.get()).
                append('\n')
                .append(correctorReferenceLibrary);
        return sb.toString();
    }
}
