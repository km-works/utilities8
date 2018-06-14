package kmworks.util.ds.rng;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import kmworks.util.lambda.LambdaUtil;
import kmworks.util.lambda.Predicate1;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class IntRangeUtil {

    private IntRangeUtil() {}

    public static IntPredicate of(@Nonnull final Predicate1<Integer> p) {
        Objects.requireNonNull(p);
        return (ch) -> p.test(ch);
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

    public static Set<Integer> codepointsFrom(CharSequence s) {
        ImmutableSet.Builder<Integer> builder = new ImmutableSet.Builder<>();
        int i=0;
        while (i < s.length()) {
            builder.add((int) s.charAt(i));
            i += Character.charCount(i);
        }
        return builder.build();
    }

}
