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

/**
 *
 * @author Christian P. Lerch
 * @param <T1>
 */
public abstract class Product1<T1> extends Product {
  
  @Override public final int arity() { return 1; }
  
  /**
   * Return a Product1 element by its 0-based index, casted to the captured type.
   * @param <T> type of value to be returned, as captured from the lhs
   * @param index 0-based index of the Product's elements {@code 0 <= index < arity()}
   * @throws IndexOutOfBoundsException, if {@code index >= arity() || index < 0}
   * @return 
   */
  @Override public final <T> T element(int index) {
    switch (index) {
      case 0: return (T) _1();
      default:
        throw new IndexOutOfBoundsException(String.valueOf(index));
    }
  }
  
  /**
   * Return the first value of the Product.
   * @return 
   */
  public abstract T1 _1();
  
}
