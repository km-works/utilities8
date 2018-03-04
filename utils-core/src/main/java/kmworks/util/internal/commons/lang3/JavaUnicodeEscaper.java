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
package kmworks.util.internal.commons.lang3;

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

/**
 * Translates codepoints to their Unicode escaped value suitable for Java source.
 * 
 * @since 3.2
 */
public class JavaUnicodeEscaper extends UnicodeEscaper {

    /**
     * <p>
     * Constructs a <code>JavaUnicodeEscaper</code> above the specified value (exclusive).
     * </p>
     * 
     * @param codepoint
     *            above which to escape
     * @return the newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper above(final int codepoint) {
        return outsideOf(0, codepoint);
    }

    /**
     * <p>
     * Constructs a <code>JavaUnicodeEscaper</code> below the specified value (exclusive).
     * </p>
     * 
     * @param codepoint
     *            below which to escape
     * @return the newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper below(final int codepoint) {
        return outsideOf(codepoint, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Constructs a <code>JavaUnicodeEscaper</code> between the specified values (inclusive).
     * </p>
     * 
     * @param codepointLow
     *            above which to escape
     * @param codepointHigh
     *            below which to escape
     * @return the newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper between(final int codepointLow, final int codepointHigh) {
        return new JavaUnicodeEscaper(codepointLow, codepointHigh, true);
    }

    /**
     * <p>
     * Constructs a <code>JavaUnicodeEscaper</code> outside of the specified values (exclusive).
     * </p>
     * 
     * @param codepointLow
     *            below which to escape
     * @param codepointHigh
     *            above which to escape
     * @return the newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper outsideOf(final int codepointLow, final int codepointHigh) {
        return new JavaUnicodeEscaper(codepointLow, codepointHigh, false);
    }

    /**
     * <p>
     * Constructs a <code>JavaUnicodeEscaper</code> for the specified range. This is the underlying method for the
     * other constructors/builders. The <code>below</code> and <code>above</code> boundaries are inclusive when
     * <code>between</code> is <code>true</code> and exclusive when it is <code>false</code>.
     * </p>
     * 
     * @param below
     *            int value representing the lowest codepoint boundary
     * @param above
     *            int value representing the highest codepoint boundary
     * @param between
     *            whether to escape between the boundaries or outside them
     */
    public JavaUnicodeEscaper(final int below, final int above, final boolean between) {
        super(below, above, between);
    }

    /**
     * Converts the given codepoint to a hex string of the form {@code "\\uXXXX\\uXXXX"}
     * 
     * @param codepoint
     *            a Unicode code point
     * @return the hex string for the given codepoint
     */
    @Override
    protected String toUtf16Escape(final int codepoint) {
        final char[] surrogatePair = Character.toChars(codepoint);
        return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
    }

}
