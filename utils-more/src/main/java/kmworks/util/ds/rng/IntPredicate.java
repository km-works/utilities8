package kmworks.util.ds.rng;

import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface IntPredicate {

    boolean contains(int value);

    default Predicate1<Integer> asPredicate() {
        return IntRangeUtil.asPredicate(this);
    }

    default IntPredicate negate() {
        return IntRangeUtil.negate(this);
    }

    default IntPredicate or(final IntPredicate p) {
        return IntRangeUtil.or(this, p);
    }

    default IntPredicate and(final IntPredicate p) {
        return IntRangeUtil.and(this, p);
    }

    default IntPredicate without(final IntPredicate p) {
        return IntRangeUtil.without(this, p);
    }


    static IntPredicate of(@Nonnull final Predicate1<Integer> p) {
        Objects.requireNonNull(p);
        return new IntPredicate() {
            @Override
            public boolean contains(int value) {
                return p.test(value);
            }
        };
    }

}
