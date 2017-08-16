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

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author Christian P. Lerch
 */
public final class IterUtil {
  
  private IterUtil() {}
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make() {
    @SuppressWarnings("unchecked") EmptyIterable<T> result = (EmptyIterable<T>) EmptyIterable.INSTANCE;
    return result;
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1) {
    return new SingletonIterable<T>(v1);
  }
  
  public static <T> SizedIterable<T> make(T... values) {
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4, T v5) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4, v5 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4, T v5, T v6) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4, v5, v6 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4, T v5, T v6, T v7) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4, v5, v6 , v7 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4, T v5, T v6, T v7, T v8) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4, v5, v6 , v7, v8 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4, T v5, T v6, T v7, T v8, T v9) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4, v5, v6 , v7, v8, v9 };
    return new ObjectArrayWrapper<T>(values, false);
  }
  
  /** Create an immutable SizedIterable containing the given values. */
  public static <T> SizedIterable<T> make(T v1, T v2, T v3, T v4, T v5, T v6, T v7, T v8, T v9, T v10) {
    @SuppressWarnings("unchecked") T[] values = (T[]) new Object[]{ v1, v2, v3, v4, v5, v6 , v7, v8, v9, v10 };
    return new ObjectArrayWrapper<T>(values, false);
  }

  /**
   * Create a SizedIterable wrapping the given array.  Subsequent changes to the array will be reflected in the
   * result.  (If that is not the desired behavior, make a copy instead with {@link #make(Object[])}.)
   */
  public static <T> SizedIterable<T> asIterable(T... array) {
    return new ObjectArrayWrapper<>(array);
  }
  
  /**
   * Create a SizedIterable wrapping a segment of the given array.  Elements from index {@code start} through the
   * end of the array are included.  Subsequent changes to the array will be reflected in the result; also note
   * that entire array will remain in memory until references to this segment are discarded.  (To prevent mutation
   * and potential memory leaks, make a copy instead with {@link #make(Object[], int)}.)
   * @throws IndexOutOfBoundsException  If {@code start} is an invalid index.
   */
  public static <T> SizedIterable<T> arraySegment(T[] array, int start) {
    return new ObjectArrayWrapper<>(array, start);
  }
  
  /**
   * Create a SizedIterable wrapping a segment of the given array.  Elements from index {@code start} through 
   * {@code end-1} are included (and the size is thus {@code end-start}).  Subsequent changes to the array
   * will be reflected in the result; also note that entire array will remain in memory until references to 
   * this segment are discarded.  (To prevent mutation and potential memory leaks, make a copy instead with 
   * {@link #make(Object[], int, int)}.)
   * @throws IndexOutOfBoundsException  If {@code start} and {@code end} are inconsistent with each other or
   *                                    with the length of the array.
   */
  public static <T> SizedIterable<T> arraySegment(T[] array, int start, int end) {
    return new ObjectArrayWrapper<>(array, start, end);
  }
    
  private static final class ObjectArrayWrapper<T> implements SizedIterable<T>, OptimizedLastIterable<T>, Serializable 
  {
    private final T[] _array;     // wrapped content array
    private final int _start;     // start index
    private final int _end;       // 1 + the last index
    private final boolean _refs;  // whether there may be other references to _array (allowing mutation)

    public ObjectArrayWrapper(T[] array) { this(array, 0, array.length, true); }
    
    public ObjectArrayWrapper(T[] array, int start) {
      this(array, start, array.length, true);
      if (_start < 0 || _start > _end) { throw new IndexOutOfBoundsException(); }
    }
    
    public ObjectArrayWrapper(T[] array, int start, int end) {
      this(array, start, end, true);
      if (_start < 0 || _start > _end || _end > _array.length) { throw new IndexOutOfBoundsException(); }
    }
    
    public ObjectArrayWrapper(T[] array, boolean refs) { this(array, 0, array.length, refs); }
    
    public ObjectArrayWrapper(T[] array, int start, int end, boolean refs) {
      _array = array;
      _start = start;
      _end = end;
      _refs = refs;
    }
    
    @Override public boolean isEmpty() { return _start == _end; }
    @Override public int size() { return _end-_start; }
    @Override public int size(int bound) { int result = _end-_start; return result <= bound ? result : bound; }
    @Override public boolean isInfinite() { return false; }
    @Override public boolean hasFixedSize() { return true; }
    @Override public boolean isImmutable() { return !_refs; }
    @Override public T last() { return _array[_end-1]; }
    
    @Override
    public Iterator<T> iterator() {
      return new IndexedIterator<T>() {
        @Override
        protected int size() { return _end-_start; }
        @Override
        protected T get(int i) { return _array[_start+i]; }
      };
    }
  }
  
}
