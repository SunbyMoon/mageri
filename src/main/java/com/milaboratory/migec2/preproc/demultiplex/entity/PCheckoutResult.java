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
package com.milaboratory.migec2.preproc.demultiplex.entity;

import com.milaboratory.core.sequence.nucleotide.NucleotideSequence;
import com.milaboratory.migec2.preproc.demultiplex.barcode.BarcodeSearcherResult;

public final class PCheckoutResult extends CheckoutResult {
    private final boolean masterFirst;
    private final BarcodeSearcherResult slaveResult;

    public PCheckoutResult(int sampleId, String sampleName, boolean masterFirst, boolean foundInRC,
                           BarcodeSearcherResult masterResult, BarcodeSearcherResult slaveResult) {
        super(sampleId, sampleName, foundInRC, masterResult);
        this.masterFirst = masterFirst;
        this.slaveResult = slaveResult;
    }

    @Override
    public boolean masterFirst() {
        return masterFirst;
    }

    public boolean slaveFound() {
        return slaveResult != null;
    }

    @Override
    public NucleotideSequence getUmi() {
        if (slaveResult != null)
            return masterResult.getUmi().concatenate(slaveResult.getUmi());
        else
            return masterResult.getUmi();
    }

    @Override
    public boolean isGood(byte umiQualThreshold) {
        return slaveFound() &&
                (byte) Math.min(masterResult.getUmiWorstQual(), slaveResult.getUmiWorstQual()) >= umiQualThreshold;
    }

    public BarcodeSearcherResult getSlaveResult() {
        return slaveResult;
    }
}