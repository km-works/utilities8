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

import java.util.Iterator;

/**
 * An abstract implementation of {@code Iterator} that implements {@link Iterator#remove} 
 * by throwing an {@link UnsupportedOperationException}. 
 * This simplifies the declaration of anonymous iterators.
 */
public abstract class UnmodifiableIterator<T> implements Iterator<T> {
  
  /** @throws  UnsupportedOperationException */
  @Override
  public void remove() { throw new UnsupportedOperationException(); }
  
}
