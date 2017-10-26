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
package kmworks.util.parsing;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author cpl
 */
public class SimpleParserState {
    
    /**
     * The default size for the arrays storing the memoization table's columns.
     */
    public static final int INIT_SIZE = 4096;

    /**
     * The increment for the arrays storing the memoization table's columns.
     */
    public static final int INCR_SIZE = 4096;
    
    /**
     * The reader for the character stream to be parsed.
     */
    private Reader sourceReader;

    /**
     * The number of characters consumed from the character stream.
     */
    private int charsConsumed;

    /**
     * The flag for whether the end-of-file has been reached.
     */
    private boolean eofSeen;

    /**
     * Buffer for the characters consumed so far.
     */
    private char[] sourceBuffer;

    /**
     * Create a new parser base.
     *
     * @param reader The reader for the character stream to be parsed.
     * @throws NullPointerException Signals a null file name.
     */
    public SimpleParserState(final Reader reader) {
        this(reader, INIT_SIZE - 1);
    }

    /**
     * Create a new parser base.
     *
     * @param reader The reader for the character stream to be parsed.
     * @param size The length of the character stream.
     * @throws NullPointerException Signals a null file name.
     * @throws IllegalArgumentException Signals a negative file size.
     */
    public SimpleParserState(final Reader reader, final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Negative size: " + size);
        }
        sourceReader = reader;
        charsConsumed = 0;
        eofSeen = false;
        sourceBuffer = new char[size + 1];
    }

    /**
     * Return the character at the specified index.
     *
     * @param index The index.
     * @return The character or -1 if the end-of-file has been reached.
     */
    public final int charAt(final int index) {
        // Have we seen the end-of-file?
        if (eofSeen) {
            if (index < charsConsumed - 1) {
                return sourceBuffer[index];
            } else if (index < charsConsumed) {
                return -1;
            } else {
                throw new IndexOutOfBoundsException("Parser index: " + index);
            }
        }

        // Have we already read the requested character?
        if (index < charsConsumed) {
            return sourceBuffer[index];
        } else if (index != charsConsumed) {
            throw new IndexOutOfBoundsException("Parser index: " + index);
        }

        // No, we haven't: Read another character.
        final int ch;
        try {
            ch = sourceReader.read();
        } catch (IOException ex) {
            throw new RuntimeException("charAt", ex);
        }
        final int incr = (-1 == ch) ? 1 : INCR_SIZE;

        // Do we have enough space?
        if (sourceBuffer.length <= charsConsumed) {
            growBy(incr);
        }

        if (-1 == ch) {
            // Remember the end-of-file.
            eofSeen = true;
        } else {
            // Remember the character.
            sourceBuffer[index] = (char) ch;
        }
        charsConsumed++;

        // Done.
        return ch;
    }

    /**
     * Determine whether the specified index represents the end-of-file.
     *
     * @param index The index.
     * @return <code>true</code> if the specified index represents EOF.
     */
    public final boolean isEOF(final int index) {
        return eofSeen && (index == charsConsumed - 1);
    }

    /**
     * Grow the source buffer by the specified increment.
     *
     * @param incr The increment.
     */
    private void growBy(final int incr) {
        final char[] oldBuffer = sourceBuffer;
        sourceBuffer = new char[oldBuffer.length + incr];
        System.arraycopy(oldBuffer, 0, sourceBuffer, 0, oldBuffer.length);
    }

}
