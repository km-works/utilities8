package kmworks.util.strings;

import kmworks.util.internal.commons.lang3.*;

public final class StringTransformerFactory {

    public static StringTransformer mkLookupTransformer(final CharSequence[]... lookupTable) {
        return new LookupTranslator(lookupTable);
    }

    public static StringTransformer mkAggregateTransformer(final StringTransformer... transformers) {
        return new AggregateTranslator(transformers);
    }

    public static StringTransformer mkUnicodeUnpairedSurrogateRemover() {
        return new UnicodeUnpairedSurrogateRemover();
    }

    // Octal Unescaper

    public static StringTransformer mkOctalUnescaper() {
        return new OctalUnescaper();
    }

    // Unicode Unescaper

    public static StringTransformer mkUnicodeUnescaper() {
        return new UnicodeUnescaper();
    }

    // XML Numeric Entity Unescaper

    public static StringTransformer mkXMLNumericEntityUnescaper() {
        return new NumericEntityUnescaper();
    }

    // XML Numeric Entity Escaper

    public static StringTransformer mkXMLNumericEntityEscaperBelow(final int codepoint) {
        return NumericEntityEscaper.below(codepoint);
    }

    public static StringTransformer mkXMLNumericEntityEscaperAbove(final int codepoint) {
        return NumericEntityEscaper.above(codepoint);
    }

    public static StringTransformer mkXMLNumericEntityEscaperBetween(final int codepointLow, final int codepointHigh) {
        return NumericEntityEscaper.between(codepointLow, codepointHigh);
    }

    public static StringTransformer mkXMLNumericEntityEscaperOutsideOf(final int codepointLow, final int codepointHigh) {
        return NumericEntityEscaper.outsideOf(codepointLow, codepointHigh);
    }

    // Java Unicode Escaper

    public static StringTransformer mkJavaUnicodeEscaperBelow(final int codepoint) {
        return JavaUnicodeEscaper.below(codepoint);
    }

    public static StringTransformer mkJavaUnicodeEscaperAbove(final int codepoint) {
        return JavaUnicodeEscaper.above(codepoint);
    }

    public static StringTransformer mkJavaUnicodeEscaperBetween(final int codepointLow, final int codepointHigh) {
        return JavaUnicodeEscaper.between(codepointLow, codepointHigh);
    }

    public static StringTransformer mkJavaUnicodeEscaperOutsideOf(final int codepointLow, final int codepointHigh) {
        return JavaUnicodeEscaper.outsideOf(codepointLow, codepointHigh);
    }

}
