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
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import kmworks.util.ObjectUtil;

/**
 * An ImmutableSortedSet of Unicode codepoints implemented as a contiguous vector of bits. 
 * Every member of the set is represented by a set bit in the bit vector, with the first member represented by bit[0].
 * This data strcuture is highly optimized for speed. Lookups using its {@code #contains(int)} method are made in 
 * constant time at a speed of 50.000.000 lookups/sec on a typical desktop PC. Memory usage is strictly proportional
 * to {@code span = last-first} around {@code span/8192 KB}, which means 8KB for spaning a complete Unicode Plane.
 *
 * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>
 */
public final class CodepointBitSet extends CodepointSet implements Externalizable, Serializable {
  
  private static final long serialVersionUID  = 1L;

  private static final CodepointBitSet EMPTY = new CodepointBitSet();
  
  private static final int BITS_PER_BUCKET = 64;
  private static final int BITS_MASK = BITS_PER_BUCKET - 1;
  private static final int BITS_SHIFT = 6;    // = lg_2 BITS_PER_BUCKET
  private static final long[] BIT_MASK;

  static {
    BIT_MASK = new long[BITS_PER_BUCKET];
    long mask = 1L;
    for (int i = 0; i < BITS_PER_BUCKET; i++) {
      BIT_MASK[i] = mask;
      mask = mask << 1;
    }
  }

  /*
      Static factories
  */
  public static CodepointSet of() {
    return EMPTY;
  }
  
  public static CodepointSet of(int... args) {
    return new Builder().add(args).build();
  }
  
  public static CodepointSet of(@Nonnull Iterable<Integer> iter) {
    return new Builder().addAll(iter).build();
  }
  
  public static CodepointSet of(@Nonnull SortedSet<Integer> set) {
    return new CodepointBitSet(set);
  }
  
  public static CodepointSet fromRange(int from, int to) {
    return new Builder().addRange(from, to).build();
  }
  
  public static CodepointSet fromRange(char from, char to) {
    return new Builder().addRange(from, to).build();
  }
  
  // construct the empty set
  private CodepointBitSet() {
    initEmptyInstance();
  }

  // construct this CodepointBitSet from a SortedSet
  private CodepointBitSet(@Nonnull SortedSet<Integer> sortedSet) 
          throws IllegalArgumentException { // if set contains out-of-bound members
    if (checkNotNull(sortedSet).isEmpty()) {
      initEmptyInstance();
    } else {
      final int first = checkBounds(sortedSet.first());
      final int last = checkBounds(sortedSet.last());
      initInstance(sortedSet.size(), first, last, initBuckets(last - first));
      for (int member : sortedSet) {
        setMember(member);  // no need to check each member here since we have already checked the bounds
      }
    }
  }

  // construct this CodepointBitSet from that AbstractCodepointSet view
  private CodepointBitSet(@Nonnull CodepointSet that, Integer fromMember, Integer toMember) {
    if (checkNotNull(that).isEmpty()) {
      initEmptyInstance();
    } else {
      Integer first = fromMember == null ? that.first() : firstMemberAtOrAbove(that, fromMember);
      Integer last = toMember == null ? that.last() : firstMemberBelow(that, toMember + 1);
      // thrown if toMember or fromMember is out of original bounds
      if (first == null || last == null) {
        throw new IllegalArgumentException();
      }
      final int size = countMembersBetween(that, first, last);
      if (size == 0) {
        initEmptyInstance();
      } else {
        initInstance(size, first, last, initBuckets(last - first));
        // array copy is not possible, because new first may be on a different bit position
        // but must always be aligned on bit 0 of bucket_[0]
        for (int member : that) {
          if (member >= first && member <= last) { // copy only members within new bounds
            setMember(member);
          }
        }
      }
    }
  }

  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final CodepointBitSet other = (CodepointBitSet) obj;
    return  this.size_ == other.size_ 
            && this.first_ == other.first_
            && this.last_ == other.last_
            && Arrays.equals(this.bucket_, other.bucket_);
  }

  @Override
  public int hashCode() {
    // ObjectUtil#deepHash provides content-based array hashing in contrast to 
    // ObjectUtil#hash, which provides only identity-based hashing
    return ObjectUtil.deepHash(size_, first_, last_, bucket_);  
  }
  
  @Override
  public int size() {
    return size_;
  }

  @Override
  public PeekingIterator<Integer> iterator() 
  {
    return unmodifiablePeekingIterator();
  }
  
  /* 
      Implementation of AbstractCodepointSet
  */
  
  @Override
  public CodepointSet intersect(@Nonnull CodepointSet that) {
    if (checkNotNull(that).isEmpty() || this.isEmpty() || this.hasDisjointRangeWith(that)) {
      return of();
    }
    Builder builder = new Builder();
    for (int codepoint : this) {
      if (that.contains(codepoint)) {
        builder.add(codepoint);
      }
    }
    return new CodepointBitSet(builder.build(), null, null);
  }

  @Override
  public CodepointSet union(@Nonnull CodepointSet that) {
    if (checkNotNull(that).isEmpty()) {
      return this;
    }
    if (this.isEmpty()) {
      return that;
    }
    Builder builder = new Builder().addAll(this).addAll(that);
    return new CodepointBitSet(builder.build(), null, null);
  }

  @Override
  public CodepointSet minus(@Nonnull CodepointSet that) {
    if (checkNotNull(that).isEmpty() || this.hasDisjointRangeWith(that)) {
      return this;
    }
    if (this.isEmpty()) {
      return of();
    }
    Builder builder = new Builder();
    for (int codepoint : this) {
      if (!that.contains(codepoint)) {
        builder.add(codepoint);
      }
    }
    return new CodepointBitSet(builder.build(), null, null);
  }

  @Override
  public CodepointSet complementOf(int first, int last) {
    if (checkBounds(first) > checkBounds(last)) {
      throw new IllegalArgumentException("first must not be larger than last");
    }
    Builder builder = new Builder();
    for (int codepoint = first; codepoint <= last; codepoint++) {
      if (!this.contains(codepoint)) {
        builder.add(codepoint);
      }
    }
    return new CodepointBitSet(builder.build(), null, null);
  }

  /* 
      Implementation of ImmutableSortedSet
  */
  @Override   /* ImmutableSortedSet<Integer> */
  public CodepointSet subSet(Integer fromMember, Integer toMember) {
    return new CodepointBitSet(this, fromMember, toMember);
  }

  @Override   /* ImmutableSortedSet<Integer> */
  public CodepointSet headSet(Integer toMember) {
    return new CodepointBitSet(this, null, toMember);
  }

  @Override   /* ImmutableSortedSet<Integer> */
  public CodepointSet tailSet(Integer fromMember) {
    return new CodepointBitSet(this, fromMember, null);
  }

  /*
      Implementation of abstract methods
  */
  
  @Override
  protected boolean isMember(int codepoint) {
    return (bucket_[bucketIdx(codepoint)] & BIT_MASK[bitIdx(codepoint)]) != 0;
  }
  
  @Override
  protected int firstMember() {
    return first_;
  }

  @Override
  protected int lastMember() {
    return last_;
  }

  protected void setMember(int codepoint) {
    bucket_[bucketIdx(codepoint)] |= BIT_MASK[bitIdx(codepoint)];
  }

  /*
      Implementation of Externalizable
  */

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(size_);
    out.writeInt(first_);
    out.writeInt(last_);
    out.writeObject(bucket_);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    size_ = in.readInt();
    first_ = in.readInt();
    last_ = in.readInt();
    bucket_ = (long[]) in.readObject();
  }

  
  public static class Builder {
    
    private final SortedSet<Integer> set;
    
    public Builder() {
      set = new TreeSet<>();
    }

    public Builder add(int cp) {
      set.add(checkBounds(cp));
      return this;
    }

    public Builder addRange(int from, int to) {
      for (int cp=from; cp <= to; cp++) {
        set.add(checkBounds(cp));        
      }
      return this;
    }
    
    public Builder addRange(char from, char to) {
      return addRange((int)from, (int)to);
    }

    public Builder add(int... args) {
      for (int cp : args) {
        set.add(checkBounds(cp));
      }
      return this;
    }
    
    public Builder add(String s) {
      return addAll(CodepointSetUtil.codepointsFrom(s));
    }

    public Builder addAll(Iterable<Integer> iter) {
      for (Integer cp : iter) {
        if (cp != null) {
          set.add(checkBounds(cp));
        }
      }
      return this;
    }

    public Builder remove(int cp) {
      set.remove(checkBounds(cp));
      return this;
    }

    public Builder removeRange(int from, int to) {
      for (int cp=from; cp <= to; cp++) {
        set.remove(checkBounds(cp));        
      }
      return this;
    }

    public Builder removeAll(Iterable<Integer> iter) {
      for (Integer cp : iter) {
        if (cp != null) {
          set.remove(checkBounds(cp));
        }
      }
      return this;
    }

    public CodepointSet build() {
      return CodepointBitSet.of(set);
    }
    
    public SortedSet<Integer> get() {
      return set;
    }
    
  }

  private PeekingIterator<Integer> unmodifiablePeekingIterator() {
    return Iterators.peekingIterator(new AbstractIterator<Integer>() 
    {
      int nextMember = first_ - 1;
      
      @Override protected Integer computeNext() {
        do {
          nextMember++;
        } while (nextMember <= last_ && !isMember(nextMember));
        
        if (nextMember > last_) {
          return endOfData();
        } else { return nextMember;
        }
      }
    });
  }
  
  /* 
      Private workers & data structures
  */
  private int bucketIdx(int codepoint) {
    final int result = (codepoint - first_) >> BITS_SHIFT;
    return result;
  }

  private int bitIdx(int codepoint) {
    final int result =  (codepoint - first_) & BITS_MASK;
    return result;
  }
  
  protected int bucketsCount() {
    return bucket_.length;
  }
  
  protected long[] buckets() {
    return bucket_;
  }
  
  // Private state initializers; only called from constructors

  private void initInstance(int size, int first, int last, long[] content) {
    size_ = size;
    first_ = first;
    last_ = last;
    bucket_ = content;    
  }
  
  private void initEmptyInstance() {
    initInstance(0, 0, 0, new long[0]);
  }
  
  private long[] initBuckets(int span) {
    return new long[span / BITS_PER_BUCKET + 1];
  }
  
  // Private immutable state
  
  private int size_;
  private int first_;
  private int last_;
  private long[] bucket_;

}
