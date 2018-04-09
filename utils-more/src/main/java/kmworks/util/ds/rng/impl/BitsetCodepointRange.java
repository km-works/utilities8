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
package kmworks.util.ds.rng.impl;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;

import kmworks.util.ds.rng.CodepointRange;
import kmworks.util.ds.rng.CodepointRangeUtil;
import static kmworks.util.ds.rng.CodepointRange.checkBounds;


/**
 * An ImmutableSortedSet of Unicode codepoints implemented as a contiguous vector of bits. Every member of the set is
 * represented by a set bit in the bit vector, with the first member represented by bit[0]. This data strcuture is highly
 * optimized for speed. Lookups using its {@code #contains(int)} method are made in constant time at a speed of 50.000.000
 * lookups/sec on a typical desktop PC. Memory usage is strictly proportional to {@code span = last-first} around
 * {@code span/8192 KB}, which means 8KB for spaning a complete Unicode Plane.
 *
 * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>

    public final class BitsetCodepointRange extends CodepointRange implements Externalizable, Serializable {

 */
public final class BitsetCodepointRange extends BitsetIntRange implements CodepointRange, Serializable {

    private static final long serialVersionUID = 1L;

    private BitsetCodepointRange(@Nonnull SortedSet<Integer> sortedSet) {
        super(sortedSet);
    }

    //  Static factories

    public static CodepointRange of(int... args) {
        return new Builder().add(args).build();
    }

    public static CodepointRange of(@Nonnull Iterable<Integer> iter) {
        return new Builder().addAll(iter).build();
    }

    public static CodepointRange of(@Nonnull SortedSet<Integer> set) {
        return new BitsetCodepointRange(set);
    }

    public static CodepointRange fromRange(int from, int to) {
        return new Builder().addRange(from, to).build();
    }

    public static CodepointRange fromRange(char from, char to) {
        return new Builder().addRange(from, to).build();
    }

/*

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


    // construct this BitsetCodepointRange from a SortedSet
    private BitsetCodepointRange(@Nonnull SortedSet<Integer> sortedSet)
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

    // construct this BitsetCodepointRange from that AbstractCodepointSet view
    private BitsetCodepointRange(@Nonnull CodepointRange that, Integer fromMember, Integer toMember) {
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
                // and must always be aligned on bit 0 of bucket_[0]
                for (int member : that) {
                    if (member >= first && member <= last) { // copy only members within new bounds
                        setMember(member);
                    }
                }
            }
        }
    }

    @Override
    public int size() {
        return size_;
    }

    @Override
    public PeekingIterator<Integer> iterator() {
        return unmodifiablePeekingIterator();
    }


    //  Implementation of AbstractCodepointSet

    @Override
    public CodepointRange intersect(@Nonnull CodepointRange that) {
        if (checkNotNull(that).isEmpty() || this.isEmpty() || this.hasDisjointRangeWith(that)) {
            return of();
        }
        Builder builder = new Builder();
        for (int codepoint : this) {
            if (that.contains(codepoint)) {
                builder.add(codepoint);
            }
        }
        return new BitsetCodepointRange(builder.build(), null, null);
    }

    @Override
    public CodepointRange union(@Nonnull CodepointRange that) {
        if (checkNotNull(that).isEmpty()) {
            return this;
        }
        if (this.isEmpty()) {
            return that;
        }
        return new Builder().addAll(this).addAll(that).build();
    }

    @Override
    public CodepointRange minus(@Nonnull CodepointRange that) {
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
        return builder.build();
    }

    @Override
    public CodepointRange complementOf(int first, int last) {
        if (checkBounds(first) > checkBounds(last)) {
            throw new IllegalArgumentException("first must not be larger than last");
        }
        Builder builder = new Builder();
        for (int codepoint = first; codepoint <= last; codepoint++) {
            if (!this.contains(codepoint)) {
                builder.add(codepoint);
            }
        }
        return builder.build();
    }


    //  Implementation of ImmutableSortedSet

    //@Override
    // ImmutableSortedSet<Integer>
    public CodepointRange subSet(Integer fromMember, Integer toMember) {
        return new BitsetCodepointRange(this, fromMember, toMember);
    }

    //@Override
    // ImmutableSortedSet<Integer>
    public CodepointRange headSet(Integer toMember) {
        return new BitsetCodepointRange(this, null, toMember);
    }

    //@Override
    // ImmutableSortedSet<Integer>
    public CodepointRange tailSet(Integer fromMember) {
        return new BitsetCodepointRange(this, fromMember, null);
    }


     // Implementation of abstract methods

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


    //  Implementation of Externalizable

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


    private PeekingIterator<Integer> unmodifiablePeekingIterator() {
        return Iterators.peekingIterator(new AbstractIterator<Integer>() {
            int nextMember = first_ - 1;

            @Override
            protected Integer computeNext() {
                do {
                    nextMember++;
                } while (nextMember <= last_ && !isMember(nextMember));

                return nextMember > last_ ? endOfData() : nextMember;
            }
        });
    }


    // Private workers & data structures

    private int bucketIdx(int codepoint) {
        final int result = (codepoint - first_) >> BITS_SHIFT;
        return result;
    }

    private int bitIdx(int codepoint) {
        final int result = (codepoint - first_) & BITS_MASK;
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
    private int first_;
    private int last_;
    private int size_;
    private long[] bucket_;

*/


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
            for (int cp = from; cp <= to; cp++) {
                set.add(checkBounds(cp));
            }
            return this;
        }

        public Builder addRange(char from, char to) {
            return addRange((int) from, (int) to);
        }

        public Builder add(int... args) {
            for (int cp : args) {
                set.add(checkBounds(cp));
            }
            return this;
        }

        public Builder add(String s) {
            return addAll(CodepointRangeUtil.codepointsFrom(s));
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
            for (int cp = from; cp <= to; cp++) {
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

        public CodepointRange build() {
            return BitsetCodepointRange.of(set);
        }

        public SortedSet<Integer> getSet() {
            return set;
        }

    }


}
