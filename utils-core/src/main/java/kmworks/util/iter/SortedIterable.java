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
package kmworks.util.iter;

import java.util.Comparator;
import java.util.Iterator;

/**
 * An {@code Iterable} whose elements are sorted relative to a {@code Comparator}, typically
 * provided at creation time.
 *
 * @author Christian P. Lerch
 */
public interface SortedIterable<T> extends Iterable<T> {
  /**
   * Returns the {@code Comparator} by which the elements of this iterable are ordered, or {@code
   * Ordering.natural()} if the elements are ordered by their natural ordering.
   * @return 
   */
  Comparator<? super T> comparator();

  /**
   * Returns an iterator over elements of type {@code T}. The elements are returned in
   * nondecreasing order according to the associated {@link #comparator}.
   */
  @Override Iterator<T> iterator();
}
