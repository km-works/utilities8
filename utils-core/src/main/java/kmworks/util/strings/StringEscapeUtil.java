/*
 *  Copyright (C) 2005-2017 Christian P. Lerch, Vienna, Austria.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.strings;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static kmworks.util.StringPool.PERCENT_CH;
import static kmworks.util.StringPool.UTF8_STRING;
import static kmworks.util.StringUtil.nullToEmpty;
import static kmworks.util.strings.StringTransformerFactory.*;

import kmworks.util.StringPool;
import kmworks.util.misc.Base64Utils;


/**
 * <p>Escapes and unescapes {@code String}s for
 * Java, Java Script, HTML and XML.</p>
 *
 * <p>#ThreadSafe#</p>
 *
 * @since 2.0
 */
public class StringEscapeUtil {

    /* ESCAPE TRANSLATORS */

    /**
     * Translator object for escaping Java.
     * <p>
     * While {@link #escapeJava(String)} is the expected method of use, this
     * object allows the Java escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer ESCAPE_JAVA =
            mkLookupTransformer(
                    new String[][]{
                            {"\"", "\\\""},   // " -> \"
                            {"\\", "\\\\"},   // \ -> \\
                    })
                    .with(mkLookupTransformer(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()))
                    .with(mkJavaUnicodeEscaperOutsideOf(32, 0x7f));

    /**
     * Translator object for escaping EcmaScript/JavaScript.
     * <p>
     * While {@link #escapeEcmaScript(String)} is the expected method of use, this
     * object allows the EcmaScript escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer ESCAPE_ECMASCRIPT =
            mkAggregateTransformer(
                    mkLookupTransformer(
                            new String[][]{
                                    {"'", "\\'"},
                                    {"\"", "\\\""},
                                    {"\\", "\\\\"},
                                    {"/", "\\/"}
                            }),
                    mkLookupTransformer(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
                    mkJavaUnicodeEscaperOutsideOf(32, 0x7f)
            );

    /**
     * Translator object for escaping Json.
     * <p>
     * While {@link #escapeJson(String)} is the expected method of use, this
     * object allows the Json escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.2
     */
    public static final StringTransformer ESCAPE_JSON =
            mkAggregateTransformer(
                    mkLookupTransformer(
                            new String[][]{
                                    {"\"", "\\\""},
                                    {"\\", "\\\\"},
                                    {"/", "\\/"}
                            }),
                    mkLookupTransformer(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
                    mkJavaUnicodeEscaperOutsideOf(32, 0x7f)
            );

    /**
     * Translator object for escaping XML.
     * <p>
     * While {@link #escapeXml10(String)} or {@link #escapeXml11(String)} are
     * the expected methods of use, this
     * object allows the XML escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     * @deprecated use {@link #ESCAPE_XML10} or {@link #ESCAPE_XML11} instead.
     */
    @Deprecated
    public static final StringTransformer ESCAPE_XML =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_ESCAPE()),
                    mkLookupTransformer(EntityArrays.APOS_ESCAPE())
            );

    /**
     * Translator object for escaping XML 1.0.
     * <p>
     * While {@link #escapeXml10(String)} is the expected method of use, this
     * object allows the XML escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.3
     */
    public static final StringTransformer ESCAPE_XML10 =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_ESCAPE()),
                    mkLookupTransformer(EntityArrays.APOS_ESCAPE()),
                    mkLookupTransformer(
                            new String[][]{
                                    {"\u0000", StringPool.EMPTY_STRING},
                                    {"\u0001", StringPool.EMPTY_STRING},
                                    {"\u0002", StringPool.EMPTY_STRING},
                                    {"\u0003", StringPool.EMPTY_STRING},
                                    {"\u0004", StringPool.EMPTY_STRING},
                                    {"\u0005", StringPool.EMPTY_STRING},
                                    {"\u0006", StringPool.EMPTY_STRING},
                                    {"\u0007", StringPool.EMPTY_STRING},
                                    {"\u0008", StringPool.EMPTY_STRING},
                                    {"\u000b", StringPool.EMPTY_STRING},
                                    {"\u000c", StringPool.EMPTY_STRING},
                                    {"\u000e", StringPool.EMPTY_STRING},
                                    {"\u000f", StringPool.EMPTY_STRING},
                                    {"\u0010", StringPool.EMPTY_STRING},
                                    {"\u0011", StringPool.EMPTY_STRING},
                                    {"\u0012", StringPool.EMPTY_STRING},
                                    {"\u0013", StringPool.EMPTY_STRING},
                                    {"\u0014", StringPool.EMPTY_STRING},
                                    {"\u0015", StringPool.EMPTY_STRING},
                                    {"\u0016", StringPool.EMPTY_STRING},
                                    {"\u0017", StringPool.EMPTY_STRING},
                                    {"\u0018", StringPool.EMPTY_STRING},
                                    {"\u0019", StringPool.EMPTY_STRING},
                                    {"\u001a", StringPool.EMPTY_STRING},
                                    {"\u001b", StringPool.EMPTY_STRING},
                                    {"\u001c", StringPool.EMPTY_STRING},
                                    {"\u001d", StringPool.EMPTY_STRING},
                                    {"\u001e", StringPool.EMPTY_STRING},
                                    {"\u001f", StringPool.EMPTY_STRING},
                                    {"\ufffe", StringPool.EMPTY_STRING},
                                    {"\uffff", StringPool.EMPTY_STRING}
                            }),
                    mkXMLNumericEntityEscaperBetween(0x7f, 0x84),
                    mkXMLNumericEntityEscaperBetween(0x86, 0x9f),
                    mkUnicodeUnpairedSurrogateRemover()
            );

    /**
     * Translator object for escaping XML 1.1.
     * <p>
     * While {@link #escapeXml11(String)} is the expected method of use, this
     * object allows the XML escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.3
     */
    public static final StringTransformer ESCAPE_XML11 =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_ESCAPE()),
                    mkLookupTransformer(EntityArrays.APOS_ESCAPE()),
                    mkLookupTransformer(
                            new String[][]{
                                    {"\u0000", StringPool.EMPTY_STRING},
                                    {"\u000b", "&#11;"},
                                    {"\u000c", "&#12;"},
                                    {"\ufffe", StringPool.EMPTY_STRING},
                                    {"\uffff", StringPool.EMPTY_STRING}
                            }),
                    mkXMLNumericEntityEscaperBetween(0x1, 0x8),
                    mkXMLNumericEntityEscaperBetween(0xe, 0x1f),
                    mkXMLNumericEntityEscaperBetween(0x7f, 0x84),
                    mkXMLNumericEntityEscaperBetween(0x86, 0x9f),
                    mkUnicodeUnpairedSurrogateRemover()
            );

    /**
     * Translator object for escaping HTML version 3.0.
     * <p>
     * While {@link #escapeHtml3(String)} is the expected method of use, this
     * object allows the HTML escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer ESCAPE_HTML3 =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_ESCAPE()),
                    mkLookupTransformer(EntityArrays.ISO8859_1_ESCAPE())
            );

    /**
     * Translator object for escaping HTML version 4.0.
     * <p>
     * While {@link #escapeHtml4(String)} is the expected method of use, this
     * object allows the HTML escaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer ESCAPE_HTML4 =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_ESCAPE()),
                    mkLookupTransformer(EntityArrays.ISO8859_1_ESCAPE()),
                    mkLookupTransformer(EntityArrays.HTML40_EXTENDED_ESCAPE())
            );

    /** TODO
     * Translator object for escaping individual Comma Separated Values. 
     *
     * While {@link #escapeCsv(String)} is the expected method of use, this 
     * object allows the CSV escaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     *
    public static final StringTransformer ESCAPE_CSV = new CsvEscaper();

    // TODO: Create a parent class - 'SinglePassTranslator' ?
    //       It would handle the index checking + length returning, 
    //       and could also have an optimization check method.
    static class CsvEscaper extends StringTransformer {

    private static final char CSV_DELIMITER = ',';
    private static final char CSV_QUOTE = '"';
    private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);
    private static final char[] CSV_SEARCH_CHARS =
    new char[] {CSV_DELIMITER, CSV_QUOTE, StringPool.CR_CH, StringPool.LF_CH};

     @Override public int translate(final CharSequence input, final int index, final Writer out) throws IOException {

     if(index != 0) {
     throw new IllegalStateException("CsvEscaper should never reach the [1] index");
     }

     if (StringUtils.containsNone(input.toString(), CSV_SEARCH_CHARS)) {
     out.write(input.toString());
     } else {
     out.write(CSV_QUOTE);
     out.write(StringUtils.replace(input.toString(), CSV_QUOTE_STR, CSV_QUOTE_STR + CSV_QUOTE_STR));
     out.write(CSV_QUOTE);
     }
     return Character.codePointCount(input, 0, input.length());
     }
     }
     */

    /* UNESCAPE TRANSLATORS */

    /**
     * Translator object for unescaping escaped Java.
     * <p>
     * While {@link #unescapeJava(String)} is the expected method of use, this
     * object allows the Java unescaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    // TODO: throw "illegal character: \92" as an Exception if a \ on the end of the Java (as per the compiler)?
    public static final StringTransformer UNESCAPE_JAVA =
            mkAggregateTransformer(
                    mkOctalUnescaper(),     // .between('\1', '\377'),
                    mkUnicodeUnescaper(),
                    mkLookupTransformer(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE()),
                    mkLookupTransformer(
                            new String[][]{
                                    {"\\\\", "\\"},
                                    {"\\\"", "\""},
                                    {"\\'", "'"},
                                    {"\\", ""}
                            })
            );

    /**
     * Translator object for unescaping escaped EcmaScript.
     * <p>
     * While {@link #unescapeEcmaScript(String)} is the expected method of use, this
     * object allows the EcmaScript unescaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;

    /**
     * Translator object for unescaping escaped Json.
     * <p>
     * While {@link #unescapeJson(String)} is the expected method of use, this
     * object allows the Json unescaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.2
     */
    public static final StringTransformer UNESCAPE_JSON = UNESCAPE_JAVA;

    /**
     * Translator object for unescaping escaped HTML 3.0.
     * <p>
     * While {@link #unescapeHtml3(String)} is the expected method of use, this
     * object allows the HTML unescaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer UNESCAPE_HTML3 =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_UNESCAPE()),
                    mkLookupTransformer(EntityArrays.ISO8859_1_UNESCAPE()),
                    mkXMLNumericEntityUnescaper()
            );

    /**
     * Translator object for unescaping escaped HTML 4.0.
     * <p>
     * While {@link #unescapeHtml4(String)} is the expected method of use, this
     * object allows the HTML unescaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer UNESCAPE_HTML4 =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_UNESCAPE()),
                    mkLookupTransformer(EntityArrays.ISO8859_1_UNESCAPE()),
                    mkLookupTransformer(EntityArrays.HTML40_EXTENDED_UNESCAPE()),
                    mkXMLNumericEntityUnescaper()
            );

    /**
     * Translator object for unescaping escaped XML.
     * <p>
     * While {@link #unescapeXml(String)} is the expected method of use, this
     * object allows the XML unescaping functionality to be used
     * as the foundation for a custom translator.
     *
     * @since 3.0
     */
    public static final StringTransformer UNESCAPE_XML =
            mkAggregateTransformer(
                    mkLookupTransformer(EntityArrays.BASIC_UNESCAPE()),
                    mkLookupTransformer(EntityArrays.APOS_UNESCAPE()),
                    mkXMLNumericEntityUnescaper()
            );

    /** TODO
     * Translator object for unescaping escaped Comma Separated Value entries.
     *
     * While {@link #unescapeCsv(String)} is the expected method of use, this 
     * object allows the CSV unescaping functionality to be used 
     * as the foundation for a custom translator. 
     *
     * @since 3.0
     *
    public static final StringTransformer UNESCAPE_CSV = new CsvUnescaper();

    static class CsvUnescaper extends StringTransformer {

    private static final char CSV_DELIMITER = ',';
    private static final char CSV_QUOTE = '"';
    private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);
    private static final char[] CSV_SEARCH_CHARS =
    new char[] {CSV_DELIMITER, CSV_QUOTE, StringPool.CR_CH, StringPool.LF_CH};

     @Override public int translate(final CharSequence input, final int index, final Writer out) throws IOException {

     if(index != 0) {
     throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
     }

     if ( input.charAt(0) != CSV_QUOTE || input.charAt(input.length() - 1) != CSV_QUOTE ) {
     out.write(input.toString());
     return Character.codePointCount(input, 0, input.length());
     }

     // strip quotes
     final String quoteless = input.subSequence(1, input.length() - 1).toString();

     if ( StringUtils.containsAny(quoteless, CSV_SEARCH_CHARS) ) {
     // deal with escaped quotes; ie) ""
     out.write(StringUtils.replace(quoteless, CSV_QUOTE_STR + CSV_QUOTE_STR, CSV_QUOTE_STR));
     } else {
     out.write(input.toString());
     }
     return Character.codePointCount(input, 0, input.length());
     }
     }
     */

    /* Helper functions */

    // Java and JavaScript
    //--------------------------------------------------------------------------

    /**
     * <p>Escapes the characters in a {@code String} using Java String rules.</p>
     *
     * <p>Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters {@code '\\'} and
     * {@code 't'}.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote and forward-slash (/) are escaped.</p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     */
    public static final String escapeJava(final String input) {
        return ESCAPE_JAVA.translate(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using EcmaScript String rules.</p>
     * <p>Escapes any values it finds into their EcmaScript String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters {@code '\\'} and
     * {@code 't'}.</p>
     *
     * <p>The only difference between Java strings and EcmaScript strings
     * is that in EcmaScript, a single quote and forward-slash (/) are escaped.</p>
     *
     * <p>Note that EcmaScript is best known by the JavaScript and ActionScript dialects. </p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     * @since 3.0
     */
    public static final String escapeEcmaScript(final String input) {
        return ESCAPE_ECMASCRIPT.translate(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using Json String rules.</p>
     * <p>Escapes any values it finds into their Json String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters {@code '\\'} and
     * {@code 't'}.</p>
     *
     * <p>The only difference between Java strings and Json strings
     * is that in Json, forward-slash (/) is escaped.</p>
     *
     * <p>See http://www.ietf.org/rfc/rfc4627.txt for further details. </p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     * @since 3.2
     */
    public static final String escapeJson(final String input) {
        return ESCAPE_JSON.translate(input);
    }

    /**
     * <p>Unescapes any Java literals found in the {@code String}.
     * For example, it will turn a sequence of {@code '\'} and
     * {@code 'n'} into a newline character, unless the {@code '\'}
     * is preceded by another {@code '\'}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     */
    public static final String unescapeJava(final String input) {
        return UNESCAPE_JAVA.translate(input);
    }

    /**
     * <p>Unescapes any EcmaScript literals found in the {@code String}.</p>
     *
     * <p>For example, it will turn a sequence of {@code '\'} and {@code 'n'}
     * into a newline character, unless the {@code '\'} is preceded by another
     * {@code '\'}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return A new unescaped {@code String}, {@code null} if null string input
     * @see #unescapeJava(String)
     * @since 3.0
     */
    public static final String unescapeEcmaScript(final String input) {
        return UNESCAPE_ECMASCRIPT.translate(input);
    }

    /**
     * <p>Unescapes any Json literals found in the {@code String}.</p>
     *
     * <p>For example, it will turn a sequence of {@code '\'} and {@code 'n'}
     * into a newline character, unless the {@code '\'} is preceded by another
     * {@code '\'}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return A new unescaped {@code String}, {@code null} if null string input
     * @see #unescapeJava(String)
     * @since 3.2
     */
    public static final String unescapeJson(final String input) {
        return UNESCAPE_JSON.translate(input);
    }

    // HTML and XML
    //--------------------------------------------------------------------------

    /**
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     *
     * <p>
     * For example:
     * </p>
     * <p><code>"bread" &amp; "butter"</code></p>
     * becomes:
     * <p>
     * <code>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</code>.
     * </p>
     *
     * <p>Supports all known HTML 4.0 entities, including funky accents.
     * Note that the commonly used apostrophe escape character (&amp;apos;)
     * is not a legal entity and so is not supported). </p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     * @since 3.0
     */
    public static final String escapeHtml4(final String input) {
        return ESCAPE_HTML4.translate(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     * <p>Supports only the HTML 3.0 entities. </p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @since 3.0
     */
    public static final String escapeHtml3(final String input) {
        return ESCAPE_HTML3.translate(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports HTML 4.0 entities.</p>
     *
     * <p>For example, the string {@code "&lt;Fran&ccedil;ais&gt;"}
     * will become {@code "<FranÃ§ais>"}</p>
     *
     * <p>If an entity is unrecognized, it is left alone, and inserted
     * verbatim into the result string. e.g. {@code "&gt;&zzzz;x"} will
     * become {@code ">&zzzz;x"}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * @since 3.0
     */
    public static final String unescapeHtml4(final String input) {
        return UNESCAPE_HTML4.translate(input);
    }

    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports only HTML 3.0 entities.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * @since 3.0
     */
    public static final String unescapeHtml3(final String input) {
        return UNESCAPE_HTML3.translate(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Escapes the characters in a {@code String} using XML entities.</p>
     *
     * <p>For example: {@code "bread" & "butter"} =&gt;
     * {@code &quot;bread&quot; &amp; &quot;butter&quot;}.
     * </p>
     *
     * <p>Note that XML 1.0 is a text-only format: it cannot represent control
     * characters or unpaired Unicode surrogate codepoints, even after escaping.
     * {@code escapeXml10} will remove characters that do not fit in the
     * following ranges:</p>
     *
     * <p>{@code #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]}</p>
     *
     * <p>Though not strictly necessary, {@code escapeXml10} will escape
     * characters in the following ranges:</p>
     *
     * <p>{@code [#x7F-#x84] | [#x86-#x9F]}</p>
     *
     * <p>The returned string can be inserted into a valid XML 1.0 or XML 1.1
     * document. If you want to allow more non-text characters in an XML 1.1
     * document, use {@link #escapeXml11(String)}.</p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see #unescapeXml(java.lang.String)
     * @since 3.3
     */
    public static String escapeXml10(final String input) {
        return ESCAPE_XML10.translate(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using XML entities.</p>
     *
     * <p>For example: {@code "bread" & "butter"} =&gt;
     * {@code &quot;bread&quot; &amp; &quot;butter&quot;}.
     * </p>
     *
     * <p>XML 1.1 can represent certain control characters, but it cannot represent
     * the null byte or unpaired Unicode surrogate codepoints, even after escaping.
     * {@code escapeXml11} will remove characters that do not fit in the following
     * ranges:</p>
     *
     * <p>{@code [#x1-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]}</p>
     *
     * <p>{@code escapeXml11} will escape characters in the following ranges:</p>
     *
     * <p>{@code [#x1-#x8] | [#xB-#xC] | [#xE-#x1F] | [#x7F-#x84] | [#x86-#x9F]}</p>
     *
     * <p>The returned string can be inserted into a valid XML 1.1 document. Do not
     * use it for XML 1.0 documents.</p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see #unescapeXml(java.lang.String)
     * @since 3.3
     */
    public static String escapeXml11(final String input) {
        return ESCAPE_XML11.translate(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Unescapes a string containing XML entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes.</p>
     *
     * <p>Supports only the five basic XML entities (gt, lt, quot, amp, apos).
     * Does not support DTDs or external entities.</p>
     *
     * <p>Note that numerical \\u Unicode codes are unescaped to their respective
     * Unicode characters. This may change in future releases. </p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * @see #escapeXml10(String)
     * @see #escapeXml11(String)
     */
    public static final String unescapeXml(final String input) {
        return UNESCAPE_XML.translate(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Returns a {@code String} value for a CSV column enclosed in double quotes,
     * if required.</p>
     *
     * <p>If the value contains a comma, newline or double quote, then the
     *    String value is returned enclosed in double quotes.</p>
     *
     * <p>Any double quote characters in the value are escaped with another double quote.</p>
     *
     * <p>If the value does not contain a comma, newline or double quote, then the
     *    String value is returned unchanged.</p>
     *
     * see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and
     * <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
     *
     * @param input the input CSV column String, may be null
     * @return the input String, enclosed in double quotes if the value contains a comma,
     * newline or double quote, {@code null} if null string input
     * @since 2.4
     *
    public static final String escapeCsv(final String input) {
    return ESCAPE_CSV.translate(input);
    }
     */

    /** TODO
     * <p>Returns a {@code String} value for an unescaped CSV column. </p>
     *
     * <p>If the value is enclosed in double quotes, and contains a comma, newline 
     *    or double quote, then quotes are removed. 
     * </p>
     *
     * <p>Any double quote escaped characters (a pair of double quotes) are unescaped 
     *    to just one double quote. </p>
     *
     * <p>If the value is not enclosed in double quotes, or is and does not contain a 
     *    comma, newline or double quote, then the String value is returned unchanged.</p>
     *
     * see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and
     * <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
     *
     * @param input the input CSV column String, may be null
     * @return the input String, with enclosing double quotes removed and embedded double 
     * quotes unescaped, {@code null} if null string input
     * @since 2.4
     *
    public static final String unescapeCsv(final String input) {
    return UNESCAPE_CSV.translate(input);
    }
     */

    /**
     * Translates a string into application/x-www-form-urlencoded format.
     * This method uses the UTF-8 encoding scheme to obtain the bytes for unsafe characters.
     * Note: The World Wide Web Consortium Recommendation states that UTF-8 should be used.
     * Not doing so may introduce incompatibilites.
     *
     * @param str Input string to be URL encoded
     * @return URL-encoded result
     */
    public static String escapeURI(@Nullable final String str) {
        String result = null;
        try {
            result = URLEncoder.encode(str, UTF8_STRING);
        } catch (UnsupportedEncodingException ex) { /* cannot happen */ }
        return nullToEmpty(result);
    }

    /**
     * Decodes a application/x-www-form-urlencoded string to UTF-8
     *
     * @param str
     * @return UTF-8 encoded equivalent string
     */
    public static String unescapeURI(@Nullable final String str) {
        return unescapeURI(str, StandardCharsets.UTF_8);
    }

    public static String unescapeURI(@Nullable final String str, final Charset charSet) {
        return nullToEmpty(decodeURL(str, charSet));
    }

    /**
     * Decodes a <code>application/x-www-form-urlencoded</code> string using a specific encoding scheme. The supplied
     * encoding is used to determine what characters are represented by any consecutive sequences of the form
     * "<code>%<i>xy</i></code>".
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that UTF-8 should be used. Not doing so may introduce
     * incompatibilites.</em>
     *
     * @param str the <code>String</code> to decode
     * @return the newly decoded <code>String</code>
     * @throws UnsupportedEncodingException If character encoding needs to be consulted, but named character encoding
     *                                      is not supported
     * @see URLEncoder#encode(java.lang.String, java.lang.String)
     */
    @Nullable
    private static String decodeURL(@Nonnull String str, Charset charSet) {
        checkNotNull(str);

        boolean needToChange = false;
        int numChars = str.length();
        StringBuilder sb = new StringBuilder(numChars > 500 ? numChars / 2 : numChars);
        int i = 0;

        char c;
        byte[] bytes = null;
        while (i < numChars) {
            c = str.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    i++;
                    needToChange = true;
                    break;
                case PERCENT_CH:
                    /*
                     * Starting with this instance of %, process all
                     * consecutive substrings of the form %xy. Each
                     * substring %xy will yield a byte. Convert all
                     * consecutive  bytes obtained this way to whatever
                     * character(s) they represent in the provided
                     * encoding.
                     */
                    try {
                        // (numChars-i)/3 is an upper bound for the number
                        // of remaining bytes
                        if (bytes == null) {
                            bytes = new byte[(numChars - i) / 3];
                        }
                        int pos = 0;

                        while (((i + 2) < numChars) && (c == PERCENT_CH)) {
                            int v = Integer.parseInt(str.substring(i + 1, i + 3), 16);
                            if (v < 0) {
                                throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
                            }
                            bytes[pos++] = (byte) v;
                            i += 3;
                            if (i < numChars) {
                                c = str.charAt(i);
                            }
                        }
                        // A trailing, incomplete byte encoding such as
                        // "%x" will cause an exception to be thrown
                        if ((i < numChars) && (c == PERCENT_CH)) {
                            throw new IllegalArgumentException(
                                    "URLDecoder: Incomplete trailing escape (%) pattern");
                        }
                        sb.append(new String(bytes, 0, pos, charSet));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "URLDecoder: Illegal hex characters in escape (%) pattern - "
                                        + e.getMessage());
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }
        return (needToChange ? sb.toString() : str);
    }

    public static String encodeBase64(final String plainText) {
        return encodeBase64(plainText, StandardCharsets.UTF_8);
    }

    public static String encodeBase64(final String plainText, final Charset charSet) {
        return Base64Utils.encodeToString(plainText.getBytes(charSet), false);
    }

    public static String decodeBase64(final String base64String) {
        return decodeBase64(base64String, StandardCharsets.UTF_8);
    }

    public static String decodeBase64(final String base64String, final Charset charSet) {
        return new String(Base64Utils.decodeFast(base64String), charSet);
    }

    public static String encodeUTF8(String s) {
        return new String(StandardCharsets.UTF_8.encode(s).array());
    }

}
