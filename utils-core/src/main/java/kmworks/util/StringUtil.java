/*
 * Copyright (C) 2014-2017 Christian P. Lerch, Vienna, Austria.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import javax.annotation.Nullable;
import kmworks.util.misc.Base64Utils;
import static kmworks.util.StringPool.*;
import kmworks.util.lambda.Function1;

/**
 * km-works static utility methods pertaining to {@code String} or {@code CharSequence}. Except when annotated with
 * {@code @Nullable}, none of these methods will ever return a {@code null} value.
 *
 * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>
 * @version 1.0.0
 * @since 1.0
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
     * Checks whether the given CharSequence is null or has zero length.
     *
     * <p>
     * Consider normalizing all of your your string references with {@link #nullToEmpty}. If you do, you can use
     * {@link String#isEmpty()} instead of this method, and you won't need special null-safe forms of methods like {@link
     * String#toUpperCase} either.
     *
     * @param str the CharSequence reference to check (may be {@code null})
     * @return {@code true} if the CharSequence is null or is the empty string
     */
    public static boolean isNullOrEmpty(@Nullable final CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNullOrEmpty(@Nullable final String str) {
        return isNullOrEmpty((CharSequence) str);
    }

    @Deprecated
    public static boolean isEmpty(final String str) {
        return isNullOrEmpty(str);
    }

    public static boolean hasLength(@Nullable final CharSequence str) {
        return !isNullOrEmpty(str);
    }

    public static boolean hasLength(@Nullable final String str) {
        return !isNullOrEmpty(str);
    }

    /**
     * Checks whether the given CharSequence is not null and contains any non-whitespace characters. Examples:
     * <p>
     * <pre class="code">
     * StringUtils.hasText(null) = false StringUtils.hasText("") = false StringUtils.hasText(" ") = false
     * StringUtils.hasText("12345") = true StringUtils.hasText(" 12345 ") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be {@code null})
     * @return {@code true} if the CharSequence is not {@code null}, its length is greater than 0, and it does not
     * solely contain whitespace characters
     * @see Character#isWhitespace
     */
    public static boolean hasText(@Nullable CharSequence str) {
        if (!isNullOrEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasText(@Nullable String str) {
        return hasText((CharSequence) str);
    }

    /**
     * Checks whether the given CharSequence is null or contains only whitespace characters.
     *
     * @param str
     * @return
     */
    public static boolean isNullOrBlank(@Nullable final CharSequence str) {
        return !hasText(str);
    }

    public static boolean isNullOrBlank(@Nullable final String str) {
        return isNullOrBlank((CharSequence) str);
    }

    /**
     * Returns the given CharSequence if it is non-null; the empty CharSequence otherwise.
     *
     * @param str the CharSequence to test and possibly return
     * @return {@code str} itself if it is non-null; {@code (CharSequence) ""} if it is null
     */
    public static CharSequence nullToEmpty(@Nullable final CharSequence str) {
        return (str == null) ? (CharSequence) EMPTY_STRING : str;
    }

    public static String nullToEmpty(@Nullable final String str) {
        return (str == null) ? EMPTY_STRING : str;
    }

    /**
     * Returns the given CharSequence if it is nonempty; {@code null} otherwise.
     *
     * @param str the CharSequence to test and possibly return
     * @return {@code str} itself if it is nonempty; {@code null} if it is empty or null
     */
    @Nullable
    public static CharSequence emptyToNull(@Nullable final CharSequence str) {
        return isNullOrEmpty(str) ? null : str;
    }

    @Nullable
    public static String emptyToNull(@Nullable final String str) {
        return isNullOrEmpty(str) ? null : str;
    }

    /**
     * Returns the given CharSequence if it is neither null nor empty, the given defaultValue otherwise.
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static CharSequence nvl(@Nullable final CharSequence str, @Nullable final CharSequence defaultValue) {
        return !isNullOrEmpty(str) ? nullToEmpty(str) : nullToEmpty(defaultValue);
    }

    public static String nvl(@Nullable final String string, @Nullable final String defaultValue) {
        return !isNullOrEmpty(string) ? nullToEmpty(string) : nullToEmpty(defaultValue);
    }

    /**
     * Returns a new string that is a substring of the given string. The substring begins with the character at the
     * specified beginIdx, but measured in reverse from the end of this string, extending to the end of this string.
     *
     * @param str
     * @param beginIdx
     * @return
     */
    public static String substringReverse(@Nullable final String str, final int beginIdx) {
        return substringReverse(str, 0, beginIdx);
    }

    /**
     * Returns a new string that is a substring of this string. The substring begins at the specified beginIndex and
     * extends to the character at index endIndex - 1, all measured in reverse from the end of the string. Thus
     * beginIndex >= endIndex and result.length() == beginIndex - endIndex.
     *
     * @param str
     * @param beginIdx
     * @param endIdx
     * @return
     * @throws IndexOutOfBoundsException
     */
    public static String substringReverse(@Nullable final String str, final int beginIdx, final int endIdx)
            throws IndexOutOfBoundsException {
        final String nnString = nullToEmpty(str);
        final int len = nnString.length();
        return nnString.substring(len - endIdx, len - beginIdx);
    }

    public static int countNewlines(@Nullable final String str) {
        return countOccurencesOf(str, "\n");
    }

    public static int countOccurencesOf(@Nullable final String str, @Nullable final String sub) {
        if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
            return 0;
        }
        int count = 0, pos = 0, idx;
        while ((idx = str.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Delete any character in a given String.
     *
     * @param inString the original String
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting String
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Facade for commons-lang3 
     * @param str
     * @return 
     */
    public static String escapeJava(@Nullable final String str) {
        return nullToEmpty(org.apache.commons.lang3.StringEscapeUtils.escapeJava(str));
    }

    public static String unescapeJava(final String str) {
        return nullToEmpty(org.apache.commons.lang3.StringEscapeUtils.unescapeJava(str));
    }

    /** Translates a string into application/x-www-form-urlencoded format. 
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
            result = URLEncoder.encode(str, "UTF-8");
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
     * @param enc The name of a supported
     * <a href="../lang/package-summary.html#charenc">character encoding</a>.
     * @return the newly decoded <code>String</code>
     * @exception UnsupportedEncodingException If character encoding needs to be consulted, but named character encoding
     * is not supported
     * @see URLEncoder#encode(java.lang.String, java.lang.String)
     * @since 1.0
     */
    @Nullable 
    private static String decodeURL(@Nullable String str, Charset charSet) {

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
                case '%':
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

                        while (((i + 2) < numChars) && (c == '%')) {
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
                        if ((i < numChars) && (c == '%')) {
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

    public static String toUTF8(String s) {
        try {
            return new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return s;
        }
    }


    /*
   * Join a list of strings into a single string
     */
    public static String join(final Iterable<String> sl) {
        return join(sl, "");
    }

    public static String join(final Iterable<String> sl, final String sep) {
        return join(sl, sep, (String arg) -> arg /* Identity */ );
    }

    public static String join(final Iterable<String> iterable, final String sep, final Function1<String, String> fn) {
        final String theSep = sep == null ? COMMA : sep;
        final StringBuilder sb = new StringBuilder();
        for (String item : iterable) {
            sb.append(fn.apply(item)).append(theSep);
        }
        final String result = sb.toString();
        return result.substring(0, result.length() - theSep.length());
    }

    public static String appendItem(final String list, final String item, final String sep) {
        if (isNullOrEmpty(list) && isNullOrEmpty(item)) {
            return EMPTY_STRING;
        }
        if (isNullOrEmpty(list)) {
            return item;
        } else if (isNullOrEmpty(item)) {
            return list;
        } else {
            return list + (isNullOrEmpty(sep) ? COMMA : sep) + item;
        }
    }

    public static boolean isInteger(final String valStr) {
        final String val = valStr.trim();
        if (isNullOrEmpty(val)) {
            return false;
        }
        Integer intVal;
        try {
            intVal = Integer.parseInt(val);
        } catch (NumberFormatException e) {
            intVal = null;
        }
        return intVal != null;
    }

    public static String fmtIntegerZeroPadded(final int number, final int digits) {
        final String fmtString = "%1$0" + digits + "d";
        return String.format(fmtString, number);
    }

    public static String fmtISODateTime(final Date date) {
        return fmtDateTime(date, "yyyy-MM-dd'T'HH:mm:ss");
    }

    public static String fmtISODate(final Date date) {
        return fmtDateTime(date, "yyyy-MM-dd");
    }

    public static String fmtDateTime(final Date dateTime, final String simpleDateTimeFormat) {
        final SimpleDateFormat sdf = new SimpleDateFormat(simpleDateTimeFormat);
        return sdf.format(dateTime);
    }

    public static String fmtMilliseconds(final long msec, final String simpleDateTimeFormat) {
        return fmtDateTime(new Date(msec), simpleDateTimeFormat);
    }

    /**
     * Utility to normalize whitespace in a String, i.e. collapses any sequence whitespaces (spaces, tabs, linefeeds
     * etc) into a single ' ' (blank) character. Note: doesn't trim() the string, however, whitespace at the beginning
     * and end is normalized too.
     *
     * @param str String to normalize
     * @return normalized version of str.
     */
    public static String normalizeWhitespace(final String str) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        final StringBuilder buf = new StringBuilder();
        final CharacterIterator iter = new StringCharacterIterator(str);
        boolean inWhitespace = false; // Flag set if we're in a second consecutive whitespace
        for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
            if (Character.isWhitespace(c)) {
                if (!inWhitespace) {
                    buf.append(' ');
                    inWhitespace = true;
                }
            } else {
                inWhitespace = false;
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static String removeQuotes(final String text) {
        String result;
        int len = 0;
        if (text.startsWith(DQ) && text.endsWith(DQ)) {
            len = 1;
        } else if (text.startsWith(SQ) && text.endsWith(SQ)) {
            len = 1;
        } else if (text.startsWith("&quot;") && text.endsWith("&quot;")) {
            len = 6;
        }
        result = text.substring(len, text.length() - len);
        return result;
    }

    public static String repeat(final String part, final int count) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(part);
        }
        return sb.toString();
    }

    public static String sqlEscSQ(final String sqlString) {
        final String result = sqlString.replaceAll(SQ, "''");
        return result;
    }

    public static String sanitizeFilenameString(final String path) {
        String result = path;
        result = result.replaceAll("ö", "oe");
        result = result.replaceAll("ä", "ae");
        result = result.replaceAll("ü", "ue");
        result = result.replaceAll("Ö", "Oe");
        result = result.replaceAll("Ä", "Ae");
        result = result.replaceAll("Ü", "Ue");
        result = result.replaceAll("ß", "sz");
        result = result.replaceAll("€", "EUR");
        result = result.replaceAll("\\[", "(");
        result = result.replaceAll("]", ")");
        result = result.replaceAll("\\{", "(");
        result = result.replaceAll("}", ")");
        result = result.replaceAll(Matcher.quoteReplacement("\\"), "_");
        result = result.replaceAll("/", "_");
        result = result.replaceAll(":", "_");
        result = result.replaceAll("\\*", "_");
        result = result.replaceAll("\\?", "_");
        result = result.replaceAll("\\+", "_");
        result = result.replaceAll(DQ, "_");
        result = result.replaceAll(SQ, "_");
        result = result.replaceAll("<", "_");
        result = result.replaceAll(">", "_");
        /*
    result = result.replaceAll("`", "_");
    result = result.replaceAll("~", "_");
    result = result.replaceAll("!", "_");
    result = result.replaceAll("%", "_");
    result = result.replaceAll("^", "_");
    result = result.replaceAll("°", "_");
    result = result.replaceAll("&", "_");
    result = result.replaceAll("|", "_");
    result = result.replaceAll("@", "_");
         */
        return result;
    }

    //---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    // From org.springframework:spring-core:4.1.2.RELEASE
    //      org.springframework.util.StringUtils
    //---------------------------------------------------------------------
    /**
     * Append the given String to the given String array, returning a new array consisting of the input array contents
     * plus the given String.
     *
     * @param array the array to append to (can be {@code null})
     * @param str the String to append
     * @return the new array (never {@code null})
     */
    public static String[] addStringToArray(String[] array, String str) {
        if (ArrayUtil.isNullOrEmpty(array)) {
            return new String[]{str};
        }
        String[] newArr = new String[array.length + 1];
        System.arraycopy(array, 0, newArr, 0, array.length);
        newArr[array.length] = str;
        return newArr;
    }

    /**
     * Concatenate the given String arrays into one, with overlapping array elements included twice.
     * <p>
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were {@code null})
     */
    public static String[] concatStringArrays(String[] array1, String[] array2) {
        if (ArrayUtil.isNullOrEmpty(array1)) {
            return array2;
        }
        if (ArrayUtil.isNullOrEmpty(array2)) {
            return array1;
        }
        String[] newArr = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArr, 0, array1.length);
        System.arraycopy(array2, 0, newArr, array1.length, array2.length);
        return newArr;
    }

    /**
     * Merge the given String arrays into one, with overlapping array elements only included once.
     * <p>
     * The order of elements in the original arrays is preserved (with the exception of overlapping elements, which are
     * only included on their first occurrence).
     *
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were {@code null})
     */
    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if (ArrayUtil.isNullOrEmpty(array1)) {
            return array2;
        }
        if (ArrayUtil.isNullOrEmpty(array2)) {
            return array1;
        }
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(array1));
        for (String str : array2) {
            if (!result.contains(str)) {
                result.add(str);
            }
        }
        return toStringArray(result);
    }

    /**
     * Turn given source String array into sorted array.
     *
     * @param array the source array
     * @return the sorted array (never {@code null})
     */
    public static String[] sortStringArray(String[] array) {
        if (ArrayUtil.isNullOrEmpty(array)) {
            return new String[0];
        }
        Arrays.sort(array);
        return array;
    }

    /**
     * Copy the given Collection into a String array. The Collection must contain String elements only.
     *
     * @param collection the Collection to copy
     * @return the String array ({@code null} if the passed-in Collection was {@code null})
     */
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }

    /**
     * Copy the given Enumeration into a String array. The Enumeration must contain String elements only.
     *
     * @param enumeration the Enumeration to copy
     * @return the String array ({@code null} if the passed-in Enumeration was {@code null})
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        }
        List<String> list = Collections.list(enumeration);
        return list.toArray(new String[list.size()]);
    }

    /**
     * Trim the elements of the given String array, calling {@code String.trim()} on each of them.
     *
     * @param array the original String array
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(String[] array) {
        if (ArrayUtil.isNullOrEmpty(array)) {
            return new String[0];
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String element = array[i];
            result[i] = (element != null ? element.trim() : null);
        }
        return result;
    }

    /**
     * Remove duplicate Strings from the given array. Also sorts the array, as it uses a TreeSet.
     *
     * @param array the String array
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(String[] array) {
        if (ArrayUtil.isNullOrEmpty(array)) {
            return array;
        }
        Set<String> set = new TreeSet<>();
        set.addAll(Arrays.asList(array));
        return toStringArray(set);
    }

    /**
     * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the result.
     *
     * @param toSplit the string to split
     * @param delimiter to split the string up with
     * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter
     * (neither element includes the delimiter); or {@code null} if the delimiter wasn't found in the given input String
     */
    public static String[] split(String toSplit, String delimiter) {
        if (!hasLength(toSplit) || !hasLength(delimiter)) {
            return null;
        }
        int offset = toSplit.indexOf(delimiter);
        if (offset < 0) {
            return null;
        }
        String beforeDelimiter = toSplit.substring(0, offset);
        String afterDelimiter = toSplit.substring(offset + delimiter.length());
        return new String[]{beforeDelimiter, afterDelimiter};
    }

    /**
     * Take an array Strings and split each element based on the given delimiter. A {@code Properties} instance is then
     * generated, with the left of the delimiter providing the key, and the right of the delimiter providing the value.
     * <p>
     * Will trim both the key and value before adding them to the {@code Properties} instance.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process
     * was null or empty
     */
    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, null);
    }

    /**
     * Take an array Strings and split each element based on the given delimiter. A {@code Properties} instance is then
     * generated, with the left of the delimiter providing the key, and the right of the delimiter providing the value.
     * <p>
     * Will trim both the key and value before adding them to the {@code Properties} instance.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element prior to attempting the split operation
     * (typically the quotation mark symbol), or {@code null} if no removal should occur
     * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process
     * was {@code null} or empty
     */
    public static Properties splitArrayElementsIntoProperties(
            String[] array, String delimiter, String charsToDelete) {

        if (ArrayUtil.isNullOrEmpty(array)) {
            return null;
        }
        Properties result = new Properties();
        for (String element : array) {
            if (charsToDelete != null) {
                element = deleteAny(element, charsToDelete);
            }
            String[] splittedElement = split(element, delimiter);
            if (splittedElement == null) {
                continue;
            }
            result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
        }
        return result;
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer. Trims tokens and omits empty tokens.
     * <p>
     * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those
     * characters can be used to separate tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@code delimitedListToStringArray}
     *
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those characters is individually
     * considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer.
     * <p>
     * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those
     * characters can be used to separate tokens. A delimiter is always a single character; for multi-character
     * delimiters, consider using {@code delimitedListToStringArray}
     *
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those characters is individually
     * considered as delimiter)
     * @param trimTokens trim the tokens via String's {@code trim}
     * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to tokens that are empty after
     * trimming; StringTokenizer will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens ({@code null} if the input String was {@code null})
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }
        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return toStringArray(tokens);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>
     * A single delimiter can consists of more than one character: It will still be considered as single delimiter
     * string, rather than as bunch of potential delimiter characters - in contrast to {@code tokenizeToStringArray}.
     *
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual
     * delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array.
     * <p>
     * A single delimiter can consists of more than one character: It will still be considered as single delimiter
     * string, rather than as bunch of potential delimiter characters - in contrast to {@code tokenizeToStringArray}.
     *
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual
     * delimiter characters)
     * @param charsToDelete a set of characters to delete. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will
     * delete all new lines and line feeds in a String.
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{str};
        }
        List<String> result = new ArrayList<>();
        if ("".equals(delimiter)) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Convert a CSV list into an array of Strings.
     *
     * @param str the input String
     * @return an array of Strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * Convenience method to convert a CSV string list to a set. Note that this will suppress duplicates.
     *
     * @param str the input String
     * @return a Set of String entries in the list
     */
    public static Set<String> commaDelimitedListToSet(String str) {
        Set<String> set = new TreeSet<>();
        String[] tokens = commaDelimitedListToStringArray(str);
        set.addAll(Arrays.asList(tokens));
        return set;
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
     * implementations.
     *
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @param prefix the String to start each element with
     * @param suffix the String to end each element with
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (coll.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = coll.iterator();
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
     * implementations.
     *
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    /**
     * Convenience method to return a Collection as a CSV String. E.g. useful for {@code toString()} implementations.
     *
     * @param coll the Collection to display
     * @return the delimited String
     */
    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    /**
     * Convenience method to return a String array as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
     * implementations.
     *
     * @param arr the array to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ArrayUtil.isNullOrEmpty(arr)) {
            return "";
        }
        if (arr.length == 1) {
            return arr[0] == null ? "null" : arr[0].toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * Convenience method to return a String array as a CSV String. E.g. useful for {@code toString()} implementations.
     *
     * @param arr the array to display
     * @return the delimited String
     */
    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    public static String getFileExt(final String fileName) {
        if (isNullOrEmpty(fileName)) {
            return null;
        }
        final int pos = fileName.lastIndexOf('.');
        return (pos < 0 ? "" : fileName.substring(pos + 1));
    }

    public static String stripFileExt(final String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return null;
        }
        final int pos = fileName.lastIndexOf('.');
        return (pos < 0 ? null : fileName.substring(0, pos));
    }

}
