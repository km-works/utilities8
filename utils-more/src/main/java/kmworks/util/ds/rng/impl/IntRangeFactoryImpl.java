package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntRangeFactoryImpl {

    public ContiguousIntRange createIntRange(int first, int last) {
        return new ContiguousIntRange(first, last);
    }

    public DiscontiguousIntRange createIntRange(@Nonnull final Set<Integer> set) {
        checkNotNull(set);
        return new DiscontiguousIntRange(set);
    }

    public IntRangeSeq createIntRange(@Nonnull List<IntRange> segments) {
        checkNotNull(segments);
        return new IntRangeSeq(segments);
    }

}
