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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import kmworks.util.ObjectUtil;

final class NTupleTypeImpl implements NTupleType {

  private static final long serialVersionUID = 1L;

  @Nonnull private final Class<?>[] types;

  NTupleTypeImpl() {
    types = new Class<?>[0];
  }

  NTupleTypeImpl(@Nonnull Class<?>[] types) {
    this.types = types;
  }

  @Override
  public int size() {
    return types.length;
  }

  //WRONG: public <T> Class<T> getNthType(int i)
  //RIGHT:
  @Override
  public Class<?> type(int i) {
    return types[i];
  }

  @Override
  public NTuple createTuple(@Nullable Object... values) {
    if ((values == null && size() == 0) || (values != null && values.length != size())) {
      throw new IllegalArgumentException(String.format("Expected %d values, not %s", 
              types.length, values == null ? "(null)" : String.valueOf(values.length)));
    }

    if (values != null) {
      for (int i = 0; i < types.length; i++) {
        final Class<?> nthType = types[i];
        final Object nthValue = values[i];
        checkSerializable(nthValue, i);
        if (nthValue != null && !nthType.isAssignableFrom(nthValue.getClass())) {
          throw new IllegalArgumentException(String.format("Expected value #%d ('%s') to be of type %s, not %s", 
                  i, nthValue, nthType, nthValue.getClass()));
        }
      }
    }

    return new NTupleImpl(this, values);
  }
  
  @Override public boolean equals(Object that) {
    if (this == that) return true;
    if (!(that instanceof NTupleTypeImpl)) return false;
    NTupleTypeImpl cast = (NTupleTypeImpl) that;
    return ObjectUtil.equals(this.types, cast.types);
  }
  
  @Override
  public int hashCode() {
    return ObjectUtil.hash(NTupleType.class, types);
  }
  
  private void checkSerializable(Object obj, int index) {
    if (obj != null) {
      checkArgument(obj instanceof Serializable, "Value #%d ('%s') must be Serializable", index, obj);
    }
  }

}
