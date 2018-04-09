package kmworks.util.ds.rng;

import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public interface IntPredicate {

    boolean contains(int value);

    default Predicate1<Integer> asPredicate() {
        return IntRangeUtil.asPredicate(this);
    }

    default IntPredicate negate() {
        return IntRangeUtil.negate(this);
    }

    default IntPredicate or(@Nonnull final IntPredicate p) {
        checkNotNull(p);
        return IntRangeUtil.or(this, p);
    }

    default IntPredicate and(@Nonnull final IntPredicate p) {
        checkNotNull(p);
        return IntRangeUtil.and(this, p);
    }

    default IntPredicate without(@Nonnull final IntPredicate p) {
        checkNotNull(p);
        return IntRangeUtil.without(this, p);
    }

    static IntPredicate of(@Nonnull final Predicate1<Integer> p) {
        checkNotNull(p);
        return p::test;
    }

}
