package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntRangeFactoryImpl {

    public ContiguousIntRange createIntRange(int first, int last) {
        return new ContiguousIntRange(first, last);
    }

    public DiscontiguousIntRange createIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        checkNotNull(sortedSet);
        return new DiscontiguousIntRange(sortedSet);
    }

    public IntRangeSeq createIntRange(@Nonnull List<IntRange> segments) {
        checkNotNull(segments);
        return new IntRangeSeq(segments);
    }

}
