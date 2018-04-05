package kmworks.util.ds.rng;

import kmworks.util.ds.rng.impl.IntRangeFactoryImpl;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.SortedSet;

public class IntRangeFactory {

    public static IntRange createIntRange(int first, int last) {
        return new IntRangeFactoryImpl().createIntRange(first, last);
    }

    public static IntRange createIntRange(@Nonnull final SortedSet<Integer> sortedSet) {
        return new IntRangeFactoryImpl().createIntRange(sortedSet);
    }

    public static IntRange createIntRange(@Nonnull List<IntRange> segments) {
        return new IntRangeFactoryImpl().createIntRange(segments);
    }

}
