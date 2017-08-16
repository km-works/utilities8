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

import java.util.Iterator;
import java.util.NoSuchElementException;
import kmworks.util.ObjectUtil;
import kmworks.util.iter.EmptyIterator;

/**
 * A singleton object that represents the empty tuple {@code ()}.
 * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>
 */
public final class Tuple extends Product {

  private static final long serialVersionUID = 1L;
  
  // The empty tuple singleton
  private static final Tuple EMPTY_TUPLE = new Tuple();
  
  private Tuple() {}

  /**
   * Return the empty tuple singleton.
   * @return 
   */
  public static Tuple of() {
    return EMPTY_TUPLE;
  }
  
  /**
   * Create a new Tuple1 from the given value.
   * (allows the types to be inferred)
   */
  public static <T1, S1 extends T1> Tuple1<T1> of(S1 v1) {
    return new Tuple1(v1);
  }
  
  /**
   * Create a new Tuple2 from given values.
   * (allows the types to be inferred)
   */
  public static <T1, T2, S1 extends T1, S2 extends T2> Tuple2<T1, T2> of(S1 v1, S2 v2) {
    return new Tuple2(v1, v2);
  }
  
  @Override public int arity() {
    return 0;
  }

  @Override public Object element(int index) {
    throw new NoSuchElementException("The empty tuple has no elements");
  }
  
  @Override public Iterator<Object> iterator() {
    return EmptyIterator.of();
  }
  
  @Override public int hashCode() {
    return ObjectUtil.hash(Tuple.class);
  }
  
  @Override public boolean equals(Object that) {
    // Since there is only one instance of this class, the EMPTY_TUPLE singleton,
    // we just need to check for identity
    return this == that;
  }
  
  @Override public String toString() {
    return String.format("#()");
  }
  
}
