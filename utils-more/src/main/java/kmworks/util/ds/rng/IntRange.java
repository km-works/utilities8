package kmworks.util.ds.rng;

import com.google.common.collect.PeekingIterator;
import kmworks.util.iter.SortedIterable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;
import static kmworks.util.StringPool.MUST_NOT_BE_NULL_MSG;

/**
 * Non-empty, not necessarily contiguous, closed range of integers.
 *
 * @author cpl
 */
public interface IntRange extends IntPredicate, SortedIterable<Integer>, Comparable<IntRange> {

    static IntRange of(Integer... values) {
        return IntRangeBuilder.noBounds().addAll(Arrays.asList(values)).build();
    }

    Bounds bounds();

    int first();

    int last();

    int size();

    default boolean isEmpty() {
        return false;
    }

    default int span() {
        return last() - first() + 1;
    }

    default boolean overlaps(IntRange that) {
        return isInRange(that.first(), this) || isInRange(this.first(), that);
    }

    default boolean isInRange(int value, IntRange rng) {
        return value >= rng.first() && value <= rng.last();
    }

    boolean equals(Object obj);

    int hashCode();

    SortedSet<Integer> asSet();

    @Override
    PeekingIterator<Integer> iterator();

    @Override
    default Comparator<Integer> comparator() {
        return COMPARATOR;
    }

    default int compareTo(IntRange other) {
        return new Integer(this.first()).compareTo(other.first());
    }

    static boolean memberEquals(IntRange a, IntRange b) {
        return memberEquals(a.asSet(), b.asSet());
    }

    static boolean memberEquals(SortedSet<Integer> a, SortedSet<Integer> b) {
        if (a.size() != b.size()) return false;
        Iterator<Integer> aIter = a.iterator(), bIter = b.iterator();
        while (aIter.hasNext()) {
            if (!aIter.next().equals(bIter.next())) return false;
        }
        return true;
    }

    Comparator<Integer> COMPARATOR = (a, b) -> {
        checkNotNull(a, "1st argument " + MUST_NOT_BE_NULL_MSG);
        checkNotNull(b, "2nd argument " + MUST_NOT_BE_NULL_MSG);
        return a.compareTo(b);
    };

    Bounds NO_BOUNDS = new Bounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
    Bounds UNICODE_BOUNDS = new Bounds(0, 1114111);

    static int checkBounds(Integer value, Bounds bounds) {
        checkNotNull(value, "Null value not allowed");
        if (bounds == NO_BOUNDS) return value;
        if (value < bounds.lower || value > bounds.upper) {
            throw new IllegalArgumentException(String.format("Value %d out of bounds [%d, %d]",
                    value, bounds.lower, bounds.upper));
        }
        return value;
    }

    class Bounds {
        int lower;
        int upper;

        public Bounds(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

/*
    IntRange intersect(IntRange that);
    IntRange union(IntRange that);
    IntRange minus(IntRange that);
    IntRange complementOf(int first, int last);
*/
}
