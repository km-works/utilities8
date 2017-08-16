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

import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import kmworks.util.ObjectUtil;

final class NTupleImpl implements NTuple {
  
  private static final long serialVersionUID = 1L;

  @Nonnull private final NTupleType type;
  @Nonnull private final Object[] values;

  NTupleImpl(@Nonnull NTupleType type, @Nullable Object[] values) {
    this.type = type;
    if (values == null || values.length == 0) {
      this.values = new Object[0];
    } else {
      this.values = new Object[values.length];
      System.arraycopy(values, 0, this.values, 0, values.length);
    }
  }

  @Override
  public NTupleType type() {
    return type;
  }

  @Override
  public int size() {
    return values.length;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T value(int i) {
    return (T) values[i];
  }

  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (this == object) {
      return true;
    }

    if (!(object instanceof NTupleImpl)) {
      return false;
    }

    final NTupleImpl cast = (NTupleImpl) object;
    if (cast.size() != size()) {
      return false;
    }

    /*
    final int size = size();
    for (int i = 0; i < size; i++) {
      final Object thisNthValue = value(i);
      final Object otherNthValue = other.value(i);
      if ((thisNthValue == null && otherNthValue != null)
              || (thisNthValue != null && !thisNthValue.equals(otherNthValue))) {
        return false;
      }
    }
    */
    return  this.type.equals(cast.type) 
            && ObjectUtil.equals(this.values, cast.values);
  }

  @Override
  public int hashCode() {
    return ObjectUtil.hash(NTuple.class, type, values);
  }

  @Override
  public String toString() {
    return Arrays.toString(values);
  }
}
