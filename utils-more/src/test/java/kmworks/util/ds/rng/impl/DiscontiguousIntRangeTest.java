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
 *  along withBounds this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;
import kmworks.util.ds.rng.IntRangeBuilder;
import kmworks.util.ds.rng.IntRangeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author cpl
 */
public class DiscontiguousIntRangeTest {

    public DiscontiguousIntRangeTest() {
    }

    private void pln(String s) {
        System.out.println(s);
    }

    @Test
    public void testContains() {
        IntRange range = IntRangeFactory.noBounds().createSegmentedIntRange(
                IntRangeBuilder.noBounds()
                        .addRange(10, 20)
                        .add(30)
                        .addRange(40, 50)
                        .build());

        assertTrue(range.contains(15));
        assertTrue(range.contains(30));
        assertTrue(range.contains(40));
        assertFalse(range.contains(29));
        assertFalse(range.contains(31));
        assertFalse(range.contains(5));
        assertFalse(range.contains(39));
        assertFalse(range.contains(51));
    }

}
