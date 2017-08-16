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

import javax.annotation.Nullable;
import kmworks.util.ObjectUtil;

/**
 *
 * @author Christian P. Lerch
 * @param <T1>
 */
public final class Tuple1<T1> extends Product1<T1> {
  
  private static final long serialVersionUID = 1L;

  @Nullable private final T1 v1;

  Tuple1(@Nullable T1 v1) {
    this.v1 = checkSerializable(v1, 1);
  }

  @Override @Nullable public T1 _1() {
    return v1;
  }

  @Override public int hashCode() {
    return ObjectUtil.hash(Tuple1.class, v1);
  }
  
  @Override public boolean equals(Object that) {
    if (this == that) return true;
    if (!(that instanceof Tuple1<?>)) return false;
    Tuple1<?> cast = (Tuple1<?>) that;
    return v1 == null ? cast.v1 == null : v1.equals(cast.v1);
  }
  
  @Override public String toString() {
    return String.format("#(%s)", v1);
  }
  
}
