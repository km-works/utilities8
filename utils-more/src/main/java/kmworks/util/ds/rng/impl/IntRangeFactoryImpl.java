package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntRangeFactoryImpl {

    public CompactIntRange createIntRange(int first, int last) {
        return new CompactIntRange(first, last);
    }

    public BitsetIntRange createIntRange(@Nonnull final Set<Integer> set) {
        checkNotNull(set);
        return new BitsetIntRange(set);
    }

    public SegmentedIntRange createIntRange(@Nonnull final List<IntRange> pieces) {
        checkNotNull(pieces);
        return new SegmentedIntRange(pieces);
    }

}
