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
package kmworks.util.base;

import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;
import kmworks.util.ObjectUtil;
import kmworks.util.iter.EmptyIterator;
import kmworks.util.iter.SingletonIterator;

/**
 *
 * @author Christian P. Lerch
 */
public abstract class Option<T> implements Iterable<T> {
  
  private static final Option NONE = new None();
  
  public static <T, S extends T> Option<T> of(@Nullable S expr) {
    if (expr != null) {
      return new Some<>(expr);
    } else {
      return of();
    }
  }
  
  public static <T> Option<T> of() {
    return NONE;
  }
  
  public abstract T get();
  
  public abstract boolean isDefined();
  
  public final boolean isEmpty() {
    return !isDefined();
  }
  
  public final <S extends T> T getOrElse(@Nullable S defaultValue) {
    if (isDefined()) {
      return get();
    } else {
      return defaultValue;
    }
  }
  
  public final T orNull() {
    return getOrElse(null);
  }
  
  @Override public final Iterator<T> iterator() {  
    if (isDefined()) {
      return SingletonIterator.<T>of(get());
    } else {
      return EmptyIterator.<T>of();
    }
  }
  
  @Override public final int hashCode() {
    if (isDefined()) {
      return ObjectUtil.hash(Option.class, Some.class, get());
    } else {
      return ObjectUtil.hash(Option.class, None.class);
    }
  }
  
  @Override public final boolean equals(@Nullable Object that) {
    if (this == that) return true;
    if (!(that instanceof Option<?>)) return false;
    Option<?> cast = (Option<?>) that;
    return isDefined() ? this.get().equals(cast.get()) : true;
  }
  
  public static class Some<T> extends Option<T> {

    private final T value;
    
    public <S extends T> Some(S value) {
      this.value = value;
    }

    @Override
    public T get() {
      return value;
    }

    @Override
    public boolean isDefined() {
      return true;
    }
  }
  
  public static class None<T> extends Option<T> {

    @Override
    public T get() {
      throw new NoSuchElementException("None.get()");
    }

    @Override
    public boolean isDefined() {
      return false;
    }
  }
    
}
