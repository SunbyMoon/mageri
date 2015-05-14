/*
 * Copyright 2013-2015 Mikhail Shugay (mikhail.shugay@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified on 7.5.2015 by mikesh
 */

package com.milaboratory.oncomigec.core.genomic;

import com.milaboratory.core.sequence.nucleotide.NucleotideSequence;
import com.milaboratory.oncomigec.pipeline.input.InputStreamWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class BedGenomicInfoProvider implements GenomicInfoProvider {
    private final static Pattern correctBedLine = Pattern.compile("^\\S+[\t ]+\\d+[\t ]+\\d+[\t ]+\\S+[\t ]+\\d+[\t ]+[+-]");
    private final Map<String, GenomicInfo> records = new HashMap<>();
    private final List<Contig> contigs;

    public BedGenomicInfoProvider(InputStreamWrapper bedRecords, InputStreamWrapper contigRecords) throws IOException {
        InputStream inputStream = contigRecords.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String[] splitLine;

        Map<String, Contig> contigByName = new HashMap<>();

        while ((line = reader.readLine()) != null) {
            splitLine = line.split("\t");
            Contig contig = new Contig(splitLine[0], splitLine[1], Integer.parseInt(splitLine[2]));
            contigByName.put(contig.getID(), contig);
        }

        this.contigs = new ArrayList<>(contigByName.values());

        inputStream = bedRecords.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((line = reader.readLine()) != null) {
            if (correctBedLine.matcher(line).find()) {
                splitLine = line.split("[\t ]+");
                GenomicInfo genomicInfo = new GenomicInfo(
                        contigByName.get(splitLine[0]),
                        Integer.parseInt(splitLine[1]),
                        Integer.parseInt(splitLine[2]),
                        splitLine[5].equals("+"));
                records.put(splitLine[3], genomicInfo);
            }
        }
    }

    @Override
    public GenomicInfo get(String name, NucleotideSequence sequence) {
        return records.get(name);
    }

    @Override
    public List<Contig> getContigs() {
        return Collections.unmodifiableList(contigs);
    }
}
