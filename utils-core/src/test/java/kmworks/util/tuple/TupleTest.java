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
package kmworks.util.tuple;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christian P. Lerch
 */
public class TupleTest {

    public TupleTest() {
    }

    /*
      Equality
    */

    @Test
    public void testEquals() {
        Tuple t1 = Tuple.of();
        Tuple t2 = Tuple.of();
        assertEquals(t1, t2);
    }

    @Test
    public void testNullValues() {
        final Tuple t1 = Tuple.builder(Object.class).setValues((Object) null).build();
        final Tuple t2 = Tuple.builder(Object.class).setValues((Object) null).build();
        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, Tuple.builder().build());
    }

}
