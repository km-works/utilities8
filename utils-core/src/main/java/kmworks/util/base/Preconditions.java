package kmworks.util.base;

import kmworks.util.StringPool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public final class Preconditions {
    private Preconditions() {}

    /**
     * Ensures that a collection passed as a parameter to the calling method is not null and not empty.
     *
     * @param collection a collection
     * @return the non-null and non-empty collection that was validated
     * @throws NullPointerException if {@code collection} is null
     * @throws IllegalArgumentException if {@code collection} is empty
     */
    //@CanIgnoreReturnValue
    public static <T, C extends Collection<T>> C checkNotEmpty(C collection) {
        return checkNotEmpty(collection, null);
    }

    /**
     * Ensures that a collection passed as a parameter to the calling method is not null and not empty.
     *
     * @param collection a collection
     * @param errorMessage the exception message to use if the check fails
     * @return the not null and not empty collection that was validated
     * @throws NullPointerException if {@code collection} is null
     * @throws IllegalArgumentException if {@code collection} is empty
     */
    //@CanIgnoreReturnValue
    public static <T, C extends Collection<T>> C checkNotEmpty(
            @Nonnull final C collection, @Nullable String errorMessage) {
        if (errorMessage == null) errorMessage = StringPool.MUST_NOT_BE_NULL_OR_EMPTY_MSG;
        if (collection == null) {
            throw new NullPointerException(errorMessage);
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return collection;
    }

    public static String checkNotEmpty(@Nonnull final String s) {
        return checkNotEmpty(s, null);
    }
    public static String checkNotEmpty(@Nonnull final String s, @Nullable String errorMessage) {
        if (errorMessage == null) errorMessage = StringPool.MUST_NOT_BE_NULL_OR_EMPTY_MSG;
        if (s == null) {
            throw new NullPointerException(errorMessage);
        }
        if (s.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return s;
    }
}
