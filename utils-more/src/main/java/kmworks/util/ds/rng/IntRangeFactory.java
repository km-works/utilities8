package kmworks.util.ds.rng;

import kmworks.util.ds.rng.impl.IntRangeFactoryImpl;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static kmworks.util.base.Preconditions.checkNotEmpty;

public class IntRangeFactory {

    private final IntRangeFactoryImpl factory;

    public static IntRangeFactory noBounds() { return new IntRangeFactory(); }
    public static IntRangeFactory withBounds(IntRange.Bounds bounds) {
        return new IntRangeFactory(bounds);
    }
    public static IntRangeFactory withUnicodeBounds() { return new IntRangeFactory(IntRange.UNICODE_BOUNDS); }

    private IntRangeFactory() {
        factory = new IntRangeFactoryImpl();
    }
    private IntRangeFactory(IntRange.Bounds bounds) {
        factory = new IntRangeFactoryImpl(bounds);
    }

    public IntRangeBuilder builder() {
        return IntRangeBuilder.withBounds(factory.getBounds());
    }

    public IntRange createCompactIntRange(int first, int last) {
        return factory.createCompactIntRange(first, last);
    }

    public IntRange createBitsetIntRange(@Nonnull final Set<Integer> set) {
        return createBitsetIntRange(new TreeSet(set));
    }

    public IntRange createBitsetIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        return factory.createBitsetIntRange(sortedSet);
    }

    public IntRange createSegmentedIntRange(@Nonnull final IntRange... pieces) {
        return createSegmentedIntRange(checkNotEmpty(Arrays.asList(checkNotNull(pieces))));
    }

    public IntRange createSegmentedIntRange(@Nonnull final List<IntRange> pieces) {
        return factory.createSegmentedIntRange(pieces);
    }

    public IntRange createSegmentedIntRange(@Nonnull final Set<Integer> set) {
        return createSegmentedIntRange(new TreeSet(set));
    }

    public IntRange createSegmentedIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        return factory.createSegmentedIntRange(sortedSet);
    }

}
