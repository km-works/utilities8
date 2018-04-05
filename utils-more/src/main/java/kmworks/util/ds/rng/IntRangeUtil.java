package kmworks.util.ds.rng;

import kmworks.util.lambda.LambdaUtil;
import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class IntRangeUtil {

    private IntRangeUtil() {}

    public static IntPredicate of(@Nonnull final Predicate1<Integer> p) {
        Objects.requireNonNull(p);
        return new IntPredicate() {
            @Override
            public boolean contains(int value) {
                return p.test(value);
            }
        };
    }

    public static IntPredicate negate(@Nonnull final IntPredicate p) {
        Objects.requireNonNull(p);
        return of(LambdaUtil.negate(asPredicate(p)));
    }

    public static IntPredicate  or(@Nonnull final IntPredicate p1, @Nonnull final IntPredicate p2) {
        Objects.requireNonNull(p1);
        Objects.requireNonNull(p2);
        return of(LambdaUtil.or(asPredicate(p1),asPredicate(p2)));
    }

    public static IntPredicate  and(@Nonnull final IntPredicate p1, @Nonnull final IntPredicate p2) {
        Objects.requireNonNull(p1);
        Objects.requireNonNull(p2);
        return of(LambdaUtil.and(asPredicate(p1),asPredicate(p2)));
    }

    public static IntPredicate without(@Nonnull final IntPredicate p1, @Nonnull final IntPredicate p2) {
        Objects.requireNonNull(p1);
        Objects.requireNonNull(p2);
        return of(LambdaUtil.andNot(asPredicate(p1),asPredicate(p2)));
    }

    public static Predicate1<Integer> asPredicate(@Nonnull final IntPredicate p) {
        Objects.requireNonNull(p);
        return i -> p.contains(i);
    }

}
