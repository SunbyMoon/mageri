package com.milaboratory.migec2.core.correct;

import com.milaboratory.migec2.util.ParameterSet;
import com.milaboratory.migec2.util.Util;
import org.jdom.Element;

public final class CorrectorParameters implements ParameterSet {
    private final double majorPvalueThreshold;
    private final double pcrEfficiency;
    private final int pcrCycles;

    private final boolean filterSingleMigs;

    private final int minMigCoverage;
    private final int minMigCount;
    private final byte minAvgQuality;

    private final double maxBasePairsMaskedRatio;

    public static CorrectorParameters DEFAULT = new CorrectorParameters(0.05, 0.85, 25,
            true, 5, 10, Util.PH33_LOW_QUAL, 0.3);

    public CorrectorParameters(double majorPvalueThreshold, double pcrEfficiency, int pcrCycles,
                               boolean filterSingleMigs,
                               int minMigCoverage, int minMigCount,
                               byte minAvgQuality, double maxBasePairsMaskedRatio) {
        this.majorPvalueThreshold = majorPvalueThreshold;
        this.pcrEfficiency = pcrEfficiency;
        this.pcrCycles = pcrCycles;
        this.filterSingleMigs = filterSingleMigs;
        this.minMigCoverage = minMigCoverage;
        this.minMigCount = minMigCount;
        this.minAvgQuality = minAvgQuality;
        this.maxBasePairsMaskedRatio = maxBasePairsMaskedRatio;
    }

    public double getMajorPvalueThreshold() {
        return majorPvalueThreshold;
    }

    public double getPcrEfficiency() {
        return pcrEfficiency;
    }

    public int getPcrCycles() {
        return pcrCycles;
    }

    public boolean filterSingleMigs() {
        return filterSingleMigs;
    }

    public int getMinMigCoverage() {
        return minMigCoverage;
    }

    public byte getMinAvgQuality() {
        return minAvgQuality;
    }

    public int getMinMigCount() {
        return minMigCount;
    }

    public double getMaxBasePairsMaskedRatio() {
        return maxBasePairsMaskedRatio;
    }

    @Override
    public Element toXml() {
        Element e = new Element("CorrectorParameters");
        e.addContent(new Element("majorPvalueThreshold").setText(Double.toString(majorPvalueThreshold)));
        e.addContent(new Element("pcrEfficiency").setText(Double.toString(pcrEfficiency)));
        e.addContent(new Element("pcrCycles").setText(Integer.toString(pcrCycles)));

        e.addContent(new Element("filterSingleMigs").setText(Boolean.toString(filterSingleMigs)));
        e.addContent(new Element("minMigCoverage").setText(Integer.toString(minMigCoverage)));
        e.addContent(new Element("minMigCount").setText(Integer.toString(minMigCount)));
        e.addContent(new Element("minAvgQuality").setText(Byte.toString(minAvgQuality)));

        e.addContent(new Element("maxBasePairsMaskedRatio").setText(Double.toString(maxBasePairsMaskedRatio)));
        return e;
    }

    public static CorrectorParameters fromXml(Element parent) {
        Element e = parent.getChild("CorrectorParameters");
        return new CorrectorParameters(
                Double.parseDouble(e.getChildTextTrim("majorPvalueThreshold")),
                Double.parseDouble(e.getChildTextTrim("pcrEfficiency")),
                Integer.parseInt(e.getChildTextTrim("pcrCycles")),

                Boolean.parseBoolean(e.getChildTextTrim("filterSingleMigs")),
                Integer.parseInt(e.getChildTextTrim("minMigCoverage")),
                Integer.parseInt(e.getChildTextTrim("minMigCount")),
                Byte.parseByte(e.getChildTextTrim("minAvgQuality")),

                Double.parseDouble(e.getChildTextTrim("maxBasePairsMaskedRatio"))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CorrectorParameters that = (CorrectorParameters) o;

        if (filterSingleMigs != that.filterSingleMigs) return false;
        if (Double.compare(that.majorPvalueThreshold, majorPvalueThreshold) != 0) return false;
        if (Double.compare(that.maxBasePairsMaskedRatio, maxBasePairsMaskedRatio) != 0) return false;
        if (minAvgQuality != that.minAvgQuality) return false;
        if (minMigCount != that.minMigCount) return false;
        if (minMigCoverage != that.minMigCoverage) return false;
        if (pcrCycles != that.pcrCycles) return false;
        if (Double.compare(that.pcrEfficiency, pcrEfficiency) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(majorPvalueThreshold);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pcrEfficiency);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + pcrCycles;
        result = 31 * result + (filterSingleMigs ? 1 : 0);
        result = 31 * result + minMigCoverage;
        result = 31 * result + minMigCount;
        result = 31 * result + (int) minAvgQuality;
        temp = Double.doubleToLongBits(maxBasePairsMaskedRatio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}