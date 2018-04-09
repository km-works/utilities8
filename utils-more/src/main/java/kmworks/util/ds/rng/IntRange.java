package kmworks.util.ds.rng;

import com.google.common.collect.PeekingIterator;
import kmworks.util.collect.ImmutableSortedSet;
import kmworks.util.iter.SortedIterable;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;
import static kmworks.util.StringPool.MUST_NOT_BE_NULL_MSG;

/** Non-empty, not necessarily contiguous, closed range of integers.
 *
 * @author cpl
 */
public interface IntRange extends IntPredicate, SortedIterable<Integer>, Comparable<IntRange> {

    int first();
    int last();
    int size();

    default boolean isEmpty() {
        return false;
    }

    @Override PeekingIterator<Integer> iterator();

    @Override default Comparator<Integer> comparator() {
        return COMPARATOR;
    }

    default int span() {
        return last() - first() + 1;
    }

    default boolean overlaps(IntRange that) {
        return isInRange(that.first(), this) || isInRange(this.first(), that);
    }

    boolean isInRange(int value, IntRange rng);

    boolean equals(Object obj);

    int hashCode();

    default int compareTo(IntRange other) {
        return new Integer(this.first()).compareTo(other.first());
    }


    static Comparator<Integer> COMPARATOR = (a, b) -> {
        checkNotNull(a, "1st argument " + MUST_NOT_BE_NULL_MSG);
        checkNotNull(b, "2nd argument " + MUST_NOT_BE_NULL_MSG);
        return a.compareTo(b);
    };

/*
    IntRange intersect(IntRange that);
    IntRange union(IntRange that);
    IntRange minus(IntRange that);
    IntRange complementOf(int first, int last);
*/
}
