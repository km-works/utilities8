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
package kmworks.util.cp;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.PeekingIterator;
import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import kmworks.util.collect.ImmutableSortedSet;

/**
 *
 * @author Christian P. Lerch
 */
public abstract class CodepointSet extends CodepointPredicate implements ImmutableSortedSet<Integer> {
  
  public static final int MIN_CODEPOINT = 0;
  public static final int MAX_CODEPOINT = 1114111;
  
  public abstract CodepointSet intersect(CodepointSet that);
  public abstract CodepointSet union(CodepointSet that);
  public abstract CodepointSet minus(CodepointSet that);
  public abstract CodepointSet complementOf(int first, int last);

  @Override   /* from CodepointPredicate */
  public final boolean contains(int codepoint) 
  {
    if (isEmpty() || codepoint < first() || codepoint > last()) {
      return false;
    }
    return isMember(codepoint);
  }
  
  @Override   /* from ImmutableSortedSet<Integer> */
  public abstract int size();
  
  @Override   /* from ImmutableSortedSet<Integer> */
  public final boolean isEmpty() {
    return size() == 0;
  }
  
  @Override   /* from ImmutableSortedSet<Integer> */
  public final boolean contains(Object o) 
  {
    if (o == null) return false;
    return o instanceof Integer ? contains((int)o) : false;
  }

  @Override   /* from ImmutableSortedSet<Integer> */
  public final boolean containsAll(Collection<?> c) 
  {
    for (Object o : c) {
      if (o == null || !(o instanceof Integer) || !contains((int)o)) {
        return false;
      }
    }
    return true;
  }
  
  @Override   /* ImmutableSortedSet<Integer> */
  public final Object[] toArray() {
    final int size = size();
    if (size == 0) {
      return new Object[0];
    }
    final Object[] result = new Object[size];
    copyIntoArray(result, 0);
    return result;
  }

  @Override   /* ImmutableSortedSet<Integer> */
  public final <T> T[] toArray(@Nonnull T[] other) {
    checkNotNull(other);
    final int size = size();
    if (other.length < size) {
      other = ObjectArrays.newArray(other, size);
    } else if (other.length > size) {
      other[size] = null;
    }
    copyIntoArray(other, 0);
    return other;
  }

  @Override   /* ImmutableSortedSet<Integer> */
  public final Integer first() 
  {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return firstMember();
  }

  @Override   /* ImmutableSortedSet<Integer> */
  public final Integer last() 
  {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return lastMember();
  }

  @Override /* Implementation for SortedIterable<Integer> */
  public final Comparator<Integer> comparator() 
  {
    return INTEGER_COMPARATOR;
  }
  
  @Override   /* from SortedIterable<Integer> */
  public abstract PeekingIterator<Integer> iterator();
  
  /**
   * Copies the contents of this immutable collection into the specified array at the specified
   * offset.  Returns {@code offset + size()}.
   */
  private int copyIntoArray(Object[] dst, int offset) {
    for (Integer i : this) {
      dst[offset++] = i;
    }
    return offset;
  }

  protected abstract boolean isMember(int codepoint);
  protected abstract int firstMember();
  protected abstract int lastMember();

  protected final boolean hasDisjointRangeWith(CodepointSet that) {
    return this.last() < that.first() || this.first() > that.last();
  }

  /*
    common static helpers
  */
  protected static int checkBounds(int codepoint) 
  {
    if (codepoint < MIN_CODEPOINT || codepoint > MAX_CODEPOINT) {
      throw new IllegalArgumentException("Codepoint out of bounds: " + codepoint);
    }
    return codepoint;
  }

  protected static int countMembersBetween(CodepointSet that, int first, int last) 
  {
    checkBounds(last);
    int count = 0;
    for (int codepoint = checkBounds(first); codepoint <= last; codepoint++) {
      if (that.contains(codepoint)) {
        count++;
      }
    }
    return count;
  }

  protected static Integer firstMemberAtOrAbove(CodepointSet that, int bound) 
  {
    checkBounds(bound);
    if (that.isEmpty() || bound < that.first() || bound > that.last()) {
      return null;
    }
    while (bound <= that.last() && !that.contains(bound)) {
      bound++;
    }
    if (bound <= that.last()) {
      return bound;
    } else {
      return null;
    }
  }

  protected static Integer firstMemberBelow(CodepointSet that, int bound) 
  {
    checkBounds(bound);
    if (that.isEmpty() || bound < that.first() || bound > that.last()) {
      return null;
    }
    do {
      bound--;
    } while (bound >= that.first() && !that.contains(bound));
    if (bound >= that.first()) {
      return bound;
    } else {
      return null;
    }
  }

  protected static final Comparator<Integer> INTEGER_COMPARATOR = new Comparator<Integer>() {
              @Override
              public int compare(@Nonnull Integer o1, @Nullable Integer o2) {
                checkNotNull(o1);
                return o1 == o2 ? 0 : o1.compareTo(o2);
              }
            };

}
