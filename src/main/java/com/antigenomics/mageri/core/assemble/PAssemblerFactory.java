/*
 * Copyright 2014-2016 Mikhail Shugay
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

package com.antigenomics.mageri.core.assemble;

import com.antigenomics.mageri.core.input.PMig;
import com.antigenomics.mageri.core.input.PreprocessorParameters;

public final class PAssemblerFactory extends AssemblerFactory<PConsensus, PMig> {
    public PAssemblerFactory(PreprocessorParameters preprocessorParameters,
                             AssemblerParameters parameters) {
        super(preprocessorParameters, parameters);
    }

    public PAssemblerFactory() {
    }

    @Override
    public PAssembler create() {
        return new PAssembler(new SAssembler(parameters, preprocessorParameters),
                new SAssembler(parameters, preprocessorParameters));
    }

    @Override
    public boolean isPairedEnd() {
        return true;
    }
}
