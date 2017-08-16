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
package kmworks.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;
import static kmworks.util.ObjectUtil.*;
import kmworks.util.io.StreamUtil;

/**
 *
 * @author Christian P. Lerch
 */
public class ObjectUtilTest {
  
  private static final Object NULL = null;
  private static final Object[] NULL_ARR_1 = new Object[] { null };
  private static final Object[] NULL_ARR_2 = new Object[] { null, null };
  private static final Object STRING_ARR_2 = new String[] { "a", "b" };
  private static final Object INT_ARR_3 = new Integer[] { 1, 2, 3 };

  private static void pln(String s) {
    System.out.println(s);
  }
  private static void pln(int s) {
    System.out.println(s);
  }
  
  public ObjectUtilTest() {
  }

  @Test
  public void testEquals() {
  }

  @Test
  public void testDeepEquals() {
  }

  @Test
  public void testHashCode() {
  }

  @Test
  public void testHash() {
    assertEquals(1, hash());
    
    assertEquals(0, hash(NULL));
    assertEquals(hash(NULL), hash(NULL_ARR_1));
    
    assertNotEquals(hash(NULL), hash(null, null));
    assertEquals(hash(null, null), hash(NULL_ARR_2));
    pln(hash(null, null));
  }

  @Test
  public void testDeepHash() {
    assertEquals(hash(), deepHash());
    
    assertEquals(hash(NULL), deepHash(NULL));
    assertEquals(deepHash(NULL), deepHash(NULL_ARR_1));
    pln(deepHash(NULL));    

    assertEquals(hash(null, null), deepHash(null, null));
    assertEquals(deepHash(null, null), deepHash(NULL_ARR_2));
    pln(deepHash(null, null));  
    
    assertEquals(hash(1), deepHash(1));
    assertEquals(hash(1, 2), deepHash(1, 2));
    assertEquals(hash(1, "a"), deepHash(1, "a"));
    
    // identity-based hashinh of arrays
    assertNotEquals(hash(1, "a", STRING_ARR_2), hash(1, "a", new String[] { "a", "b" }));
    // content-based hashing of arrays
    assertEquals(deepHash(1, "a", STRING_ARR_2), deepHash(1, "a", new String[] { "a", "b" }));
    
    assertNotEquals(hash(1, "a", STRING_ARR_2), deepHash(1, "a", STRING_ARR_2));
    pln("" + hash(1, "a", STRING_ARR_2) + " vs " + deepHash(1, "a", STRING_ARR_2));

    assertNotEquals(hash(2, "b", INT_ARR_3), deepHash(2, "b", INT_ARR_3));
    pln("" + hash(2, "b", INT_ARR_3) + " vs " + deepHash(2, "b", INT_ARR_3));
  }

  @Test
  public void testToString_Object_String() {
  }

  @Test
  public void testCompare() {
  }

  @Test
  public void testRequireNonNull_GenericType() {
  }

  @Test
  public void testRequireNonNull_GenericType_String() {
  }

  @Test
  public void testIsNull() {
  }

  @Test
  public void testNonNull() {
  }
  
}
