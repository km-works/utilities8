package kmworks.util.ds.rng;

import kmworks.util.ds.rng.impl.IntRangeFactoryImpl;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static kmworks.util.base.Preconditions.checkNotEmpty;

public class IntRangeFactory {

    public static IntRange createIntRange(int first, int last) {
        return new IntRangeFactoryImpl().createIntRange(first, last);
    }

    public static IntRange createIntRange(@Nonnull final Set<Integer> set) {
        return new IntRangeFactoryImpl().createIntRange(set);
    }

    public static IntRange createIntRangePiecewise(@Nonnull final IntRange... pieces) {
        return createIntRangePiecewise(checkNotEmpty(Arrays.asList(checkNotNull(pieces))));
    }

    public static IntRange createIntRangePiecewise(@Nonnull final List<IntRange> pieces) {
        return new IntRangeFactoryImpl().createIntRange(pieces);
    }

}
