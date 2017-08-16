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

import static com.google.common.base.Preconditions.checkArgument;
import java.io.Serializable;
import java.util.Iterator;
import kmworks.util.iter.IndexedIterator;

/**
 *
 * @author Christian P. Lerch
 */
public abstract class Product implements Iterable<Object>, Serializable {
  
  /**
   * Total number of values the Product holds.
   * @return 
   */
  public abstract int arity();
  
  /**
   * Return a Product element by its 0-based index, casted to the captured type.
   * @param <T> type of value to be returned, as captured from the lhs
   * @param index 0-based index of the Product's elements {@code 0 <= index < arity()}
   * @throws IndexOutOfBoundsException, if {@code index >= arity() || index < 0}
   * @return value of nth element
   */
  public abstract <T> T element(int index);
  

  /**
   * Implement {@code Iterable<Object>}
   * @return iterator over all product elements
   */
  @Override public Iterator<Object> iterator() {
    return new IndexedIterator<Object>() {
      @Override
      protected int size() {
        return arity();
      }

      @Override
      protected Object get(int index) {
        return element(index);
      }
    };
  }
  
  /**
   * Checks if the value to be used as the nth element of a Product is Serializable.
   * Intended to be used in Product constructors
   * @param <T> type of value to be checked, captured from the lhs
   * @param value value to be checked
   * @param index 0-based index of the Product's elements. {@code 0 <= n < size()}
   * @throws IllegalArgumentException, if check fails
   * @return unchanged value, if check succeeds 
   */
  final <T> T checkSerializable(T value, int index) {
    if (value != null) {
      checkArgument(value instanceof Serializable, "Element[%d]: %s is not Serializable", index, value);
    }
    return value;
  }
  
}
