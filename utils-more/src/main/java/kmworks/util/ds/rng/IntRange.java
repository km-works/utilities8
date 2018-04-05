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
public interface IntRange extends IntPredicate, SortedIterable<Integer> {   //ImmutableSortedSet<Integer>

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
        return this.last() >= that.first() || this.first() <= that.last();
    }

    boolean equals(Object obj);
    int hashCode();

    static Comparator<Integer> COMPARATOR = (a, b) -> {
        checkNotNull(a, "1st argument " + MUST_NOT_BE_NULL_MSG);
        checkNotNull(b, "2nd argument " + MUST_NOT_BE_NULL_MSG);
        return a.compareTo(b);
    };

/*
    CodepointSet intersect(CodepointSet that);
    CodepointSet union(CodepointSet that);
    CodepointSet minus(CodepointSet that);
    CodepointSet complementOf(int first, int last);
*/
}
