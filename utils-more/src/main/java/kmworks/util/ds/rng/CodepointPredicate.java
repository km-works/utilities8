package kmworks.util.ds.rng;

import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public interface CodepointPredicate extends IntPredicate {

    default boolean contains(char value) {
        return contains((int) value);
    }

    default Predicate1<Integer> asPredicate() {
        return CodepointRangeUtil.asPredicate(this);
    }

    default CodepointPredicate negate() {
        return CodepointRangeUtil.negate(this);
    }

    default CodepointPredicate or(@Nonnull final CodepointPredicate p) {
        checkNotNull(p);
        return CodepointRangeUtil.or(this, p);
    }

    default CodepointPredicate and(@Nonnull final CodepointPredicate p) {
        checkNotNull(p);
        return CodepointRangeUtil.and(this, p);
    }

    default CodepointPredicate without(@Nonnull final CodepointPredicate p) {
        checkNotNull(p);
        return CodepointRangeUtil.without(this, p);
    }

    static CodepointPredicate of(@Nonnull final Predicate1<Integer> p) {
        checkNotNull(p);
        return p::test;
    }

}
