/*
 * Copyright (c) 2015-2017 Christian P. Lerch, Vienna, Austria <christian.p.lerch@gmail.com>
 * All rights reserved.
 */
package kmworks.util.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Christian P. Lerch
 */
public final class IterUtil {

    private IterUtil() {
    }
    
    public static <T> boolean exists(Iterable<T> iter, Predicate<T> where) {
        return select(iter, where).size() > 0;
    }
    
    public static <T> boolean existsOne(Iterable<T> iter, Predicate<T> where) {
        return select(iter, where).size() == 1;
    }
    
    public static <T> Set<T> select(Iterable<T> iter, Predicate<T> where) {
        return setFrom(iter, it -> it, where);
    }

    public static <T, F> List<T> listFrom(
            Iterable<F> iter,
            Function<F, T> mapper) {
        return listFrom(iter, mapper, (F p) -> true);
    }

    public static <T, F> List<T> listFrom(
            Iterable<F> iter,
            Function<F, T> mapper,
            Predicate<F> where) {
        ImmutableList.Builder<T> builder = new ImmutableList.Builder<>();
        for (F item : iter) {
            if (where.test(item)) {
                builder.add(mapper.apply(item));
            }
        }
        return builder.build();
    }

    public static <T, F> Set<T> setFrom(
            Iterable<F> iter,
            Function<F, T> mapper) {
        return setFrom(iter, mapper, (F p) -> true);
    }

    public static <T, F> Set<T> setFrom(
            Iterable<F> iter,
            Function<F, T> mapper,
            Predicate<F> where) {
        ImmutableSet.Builder<T> builder = new ImmutableSet.Builder<>();
        for (F item : iter) {
            if (where.test(item)) {
                builder.add(mapper.apply(item));
            }
        }
        return builder.build();
    }

    public static <T, F> Set<T> flattenedSetFrom(
            Iterable<F> iter,
            Function<F, Iterable<T>> mapper) {
        return flattenedSetFrom(iter, mapper, (F p) -> true);
    }

    public static <T, F> Set<T> flattenedSetFrom(
            Iterable<F> iter,
            Function<F, Iterable<T>> mapper,
            Predicate<F> where) {
        ImmutableSet.Builder<T> builder = new ImmutableSet.Builder<>();
        for (F item : iter) {
            if (where.test(item)) {
                builder.addAll(mapper.apply(item));
            }
        }
        return builder.build();
    }

    public static <T> Map<String, T> nameMapFrom(
            Iterable<T> list,
            Function<T, String> mapper) {
        return nameMapFrom(list, mapper, (T p) -> true);
    }

    public static <T> Map<String, T> nameMapFrom(
            Iterable<T> list,
            Function<T, String> mapper,
            Predicate<T> condition) {
        ImmutableMap.Builder<String, T> builder = new ImmutableMap.Builder<>();
        for (T item : list) {
            if (condition.test(item)) {
                builder.put(mapper.apply(item), item);
            }
        }
        return builder.build();
    }

}
