package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntRangeFactoryImpl {

    public CompactIntRange createCompactIntRange(int first, int last) {
        return new CompactIntRange(first, last);
    }

    public BitsetIntRange createBitsetIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        checkNotNull(sortedSet);
        return new BitsetIntRange(sortedSet);
    }

    public SegmentedIntRange createSegmentedIntRange(@Nonnull final List<IntRange> pieces) {
        checkNotNull(pieces);
        return new SegmentedIntRange(pieces);
    }

    public SegmentedIntRange createSegmentedIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        checkNotNull(sortedSet);
        return new SegmentedIntRange(sortedSet);
    }

}
