package kmworks.util.ds.rng.impl;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import kmworks.util.ObjectUtil;
import kmworks.util.ds.rng.IntRange;
import kmworks.util.ds.rng.IntRangeFactory;

import javax.annotation.Nonnull;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.google.common.base.Preconditions.checkNotNull;
import static kmworks.util.StringPool.MUST_NOT_BE_EMPTY_MSG;
import static kmworks.util.base.Preconditions.checkNotEmpty;

public class BitsetIntRange extends AbstractIntRange {

    private long[] buckets;

    private static final int BITS_PER_BUCKET = 64;
    private static final int BITS_MASK = BITS_PER_BUCKET - 1;
    private static final int BITS_SHIFT = 6;    // = lg_2 of BITS_PER_BUCKET
    private static final long[] BIT_MASK;

    static {
        BIT_MASK = new long[BITS_PER_BUCKET];
        long mask = 1L;
        for (int i = 0; i < BITS_PER_BUCKET; i++) {
            BIT_MASK[i] = mask;
            mask = mask << 1;
        }
    }

    BitsetIntRange(@Nonnull SortedSet<Integer> sortedSet) {

        super(new AbstractIntRange.Initializer() {
            @Override
            public int getFirst() {
                return checkNotEmpty(sortedSet).first();
            }

            @Override
            public int getLast() {
                return checkNotEmpty(sortedSet).last();
            }

            @Override
            public int getSize() {
                return checkNotEmpty(sortedSet).size();
            }
        });

        buckets = initBuckets(span() - 1);

        for (int value : sortedSet) {
            setMember(value);
        }
    }

    BitsetIntRange(@Nonnull IntRange range, Integer fromMember, Integer toMember) {

        super(new AbstractIntRange.Initializer() {
            private Integer first, last, size;

            @Override
            public int getFirst() {
                first = fromMember == null ? checkNotNull(range).first() : firstMemberAtOrAbove(range, fromMember);
                return checkNotNull(first, "new range " + MUST_NOT_BE_EMPTY_MSG);
            }

            @Override
            public int getLast() {
                last = toMember == null ? range.last() : firstMemberBelow(range, toMember + 1);
                return checkNotNull(last, "new range " + MUST_NOT_BE_EMPTY_MSG);
            }

            @Override
            public int getSize() {
                size = countMembersBetween(range, first, last);
                if (size > 0) return size;
                else throw new IllegalArgumentException("new range " + MUST_NOT_BE_EMPTY_MSG);
            }
        });

        buckets = initBuckets(span() - 1);
        // array copy is not possible, because new first may be on a different bit position
        // and must always be aligned on bit 0 of bucket_[0]
        for (int member : range) {
            if (member >= first() && member <= last()) { // copy only members within new bounds
                setMember(member);
            }
        }

    }


    @Override
    public boolean contains(int value) {
        return value < first() || value > last() ? false : isMember(value);
    }

    @Override
    public PeekingIterator<Integer> iterator() {

        return Iterators.peekingIterator(new AbstractIterator<Integer>() {
            int nextMember = first() - 1;

            @Override
            protected Integer computeNext() {
                do {
                    nextMember++;
                } while (nextMember <= last() && !isMember(nextMember));

                return nextMember > last() ? endOfData() : nextMember;
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!canEqualWith(obj)) {
            return false;
        }
        final BitsetIntRange other = (BitsetIntRange) obj;
        if (this.size() != other.size() || this.first() != other.first() || this.last() != other.last()) {
            return false;
        }
        final int length = this.buckets.length;
        if (other.buckets.length != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (this.buckets[i] != other.buckets[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        // ObjectUtil#deepHash provides content-based array hashing in contrast to
        // ObjectUtil#hash, which provides only identity-based hashing
        return ObjectUtil.deepHash(size(), first(), last(), buckets);
    }


    public static class Builder {

        private final SortedSet<Integer> set;

        public Builder() {
            set = new TreeSet<>();
        }

        public Builder add(int value) {
            set.add(value);
            return this;
        }

        public Builder addRange(int from, int to) {
            for (int value = from; value <= to; value++) {
                set.add(value);
            }
            return this;
        }

        public Builder addRange(char from, char to) {
            return addRange((int) from, (int) to);
        }

        public Builder add(int... values) {
            for (int value : values) {
                set.add(value);
            }
            return this;
        }

        public Builder addAll(Iterable<Integer> iter) {
            for (Integer value : iter) {
                if (value != null) {
                    set.add(value);
                }
            }
            return this;
        }

        public Builder remove(int value) {
            set.remove(value);
            return this;
        }

        public Builder removeRange(int from, int to) {
            for (int value = from; value <= to; value++) {
                set.remove(value);
            }
            return this;
        }

        public Builder removeAll(Iterable<Integer> iter) {
            for (Integer value : iter) {
                if (value != null) {
                    set.remove(value);
                }
            }
            return this;
        }

        public IntRange build() {
            return IntRangeFactory.createBitsetIntRange(set);
        }

    }

    private boolean isMember(int value) {
        return (buckets[bucketIdx(value)] & BIT_MASK[bitIdx(value)]) != 0;
    }

    private void setMember(int value) {
        buckets[bucketIdx(value)] |= BIT_MASK[bitIdx(value)];
    }

    private int bucketIdx(int value) {
        return (value - first()) >> BITS_SHIFT;
    }

    private int bitIdx(int value) {
        return (value - first()) & BITS_MASK;
    }

    private long[] initBuckets(int span) {
        return new long[span / BITS_PER_BUCKET + 1];
    }

}
