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

/**
 *
 * @author Christian P. Lerch
 */
public abstract class Try<T> {
  
  public static <T, S extends T> Try<S> try_(Tryable<S> expr) 
  {
    try {
      return new Success<>(expr.eval());
    } catch (Exception ex) {
      return new Failure<>(ex);
    }
  }
  
  public abstract T get();
  
  public String failureDesc() {
    return null;
  }
  
  public abstract boolean isSuccess();
  
  public final boolean isFailure() {
    return !isSuccess();
  }
  
  public final <S extends T> T getOrElse(S defaultValue) {
    if (isSuccess()) {
      return get();
    } else {
      return defaultValue;
    }
  }
  
  public final Try<T> orElse(Try<T> defaultValue) {
    if (isSuccess()) {
      return this;
    } else {
      return defaultValue;
    }
  }
  
  
  public static class Success<T> extends Try<T> {
    
    private final T value;
    
    public Success(T value) {
      this.value = value;
    }

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public T get() {
      return value;
    }
  }
  
  public static class Failure<T> extends Try<T> {
    
    private final Exception exception;
    
    public Failure(Exception ex) {
      this.exception = ex;
    }

    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public T get() {
      throw new WrappedException(exception);
    }
    
    @Override
    public String failureDesc() {
      return exception.toString();
    }
  }
  
}
