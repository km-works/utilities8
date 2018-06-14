package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntPredicate;
import kmworks.util.ds.rng.IntRange;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkNotNull;

public class IntRangeFactoryImpl {

    private final IntRange.Bounds bounds;

    public IntRangeFactoryImpl() {
        this(IntRange.NO_BOUNDS);
    }
    public IntRangeFactoryImpl(@Nonnull final IntRange.Bounds bounds) {
        this.bounds = bounds;
    }

    public IntRange.Bounds getBounds() { return bounds; }

    public CompactIntRange createCompactIntRange(int first, int last) {
        return new CompactIntRange(first, last, bounds);
    }

    public BitsetIntRange createBitsetIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        checkNotNull(sortedSet);
        return new BitsetIntRange(sortedSet, bounds);
    }

    public SegmentedIntRange createSegmentedIntRange(@Nonnull final List<IntRange> pieces) {
        checkNotNull(pieces);
        return new SegmentedIntRange(pieces, bounds);
    }

    public SegmentedIntRange createSegmentedIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        checkNotNull(sortedSet);
        return new SegmentedIntRange(sortedSet, bounds);
    }

}
