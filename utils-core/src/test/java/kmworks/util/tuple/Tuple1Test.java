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
public class Tuple1Test {
  
  public Tuple1Test() {
  }

  @Test
  public void testOf() {
    final Tuple1<Integer> t1 = Tuple.of(1);
    final int v = t1._1();
    final int e = t1.element(0);
    assertEquals(1, v);
    assertEquals(v, e);
  }

  @Test(expected=java.lang.ClassCastException.class)
  public void testOf_2() {
    final Tuple1<Integer> t1 = Tuple.of(1);
    // final String s1 = t1._1(); // compilation-error
    final String s2 = t1.element(0);  // runtime-error
  }

  /*
      Equality
  */
  
  @Test
  public void testNullValues() {
    final Tuple1<Object> t1 = Tuple.of(null);
    final Tuple1<Object> t2 = Tuple.of(null);
    assertEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());
    assertNotEquals(t1, Tuple.of());
  }
  
  @Test
  public void testEquals1() {
    final Tuple1<Long> t1 = Tuple.of(1L);
    final Tuple1<Number> t2 = Tuple.of(1L);
    assertEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  public void testEquals2() {
    final Tuple1<Integer> t1 = Tuple.of(1);
    final Tuple1<Number> t2 = Tuple.of(1);
    assertEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  public void testEquals3() {
    final Tuple1<Integer> t1 = Tuple.of(1);
    final Tuple1<Number> t2 = Tuple.of(1L);
    assertNotEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  public void testEquals4() {
    final Tuple1<Long> t1 = Tuple.of(1L);
    final Tuple1<Number> t2 = Tuple.of(1);
    assertNotEquals(t1, t2);
    assertEquals(t1.hashCode(), t2.hashCode());
  }

  @Test
  public void testEquals5() {
    final Tuple1<Long> t1 = Tuple.of(1L);
    final Tuple1<Number> t2 = Tuple.of(1f);
    assertNotEquals(t1, t2);
    assertNotEquals(t1.hashCode(), t2.hashCode());
  }
  
  @Test
  public void testEquals6() {
    final Tuple1<Integer> t1 = Tuple.of(1);
    final Tuple1<Number> t2 = Tuple.of(1f);
    assertNotEquals(t1, t2);
    assertNotEquals(t1.hashCode(), t2.hashCode());
  }
  
  @Test
  public void testEquals7() {
    final Tuple1<Long> t1 = Tuple.of(1L);
    final Tuple1<String> t2 = Tuple.of("test");
    assertNotEquals(t1, t2);
    assertNotEquals(t1.hashCode(), t2.hashCode());
  }

  /*
      String representation
  */

  @Test
  public void testToString() {
    assertEquals("#(1)", Tuple.of(1).toString());
  }
  
}
