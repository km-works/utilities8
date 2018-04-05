/*
 * Copyright (C) 2005-2016 Christian P. Lerch <christian.p.lerch[at]gmail.com>
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
package kmworks.util.ds.rng;


import kmworks.util.ds.rng.IntRange;

/**
 * @author Christian P. Lerch
 */
public interface CodepointSet extends CodepointPredicate, IntRange {

    int CODEPOINT_MIN = 0;
    int CODEPOINT_MAX = 1114111;

    default boolean contains(char value) {
        return contains((int) value);
    }

    static int checkBounds(int codepoint) {
        if (codepoint < CODEPOINT_MIN || codepoint > CODEPOINT_MAX) {
            throw new IllegalArgumentException(String.format("Codepoint %d out of bounds [%d, %d]",
                    codepoint, CODEPOINT_MIN, CODEPOINT_MAX));
        }
        return codepoint;
    }

}
