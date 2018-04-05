package kmworks.util.ds.rng;

import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface CodepointPredicate extends IntPredicate {

    default Predicate1<Integer> asPredicate() {
        return CodepointSetUtil.asPredicate(this);
    }

    default CodepointPredicate negate() {
        return CodepointSetUtil.negate(this);
    }

    default CodepointPredicate or(final CodepointPredicate p) {
        return CodepointSetUtil.or(this, p);
    }

    default CodepointPredicate and(final CodepointPredicate p) {
        return CodepointSetUtil.and(this, p);
    }

    default CodepointPredicate without(final CodepointPredicate p) {
        return CodepointSetUtil.without(this, p);
    }

    static CodepointPredicate of(@Nonnull final Predicate1<Integer> p) {
        Objects.requireNonNull(p);
        return new CodepointPredicate() {
            @Override
            public boolean contains(int value) {
                return p.test(value);
            }
        };
    }


}
