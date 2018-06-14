package kmworks.util.ds.rng;

import kmworks.util.ds.rng.impl.IntRangeBuilderImpl;

import java.util.Iterator;
import java.util.SortedSet;

public interface IntRangeBuilder {

    static IntRangeBuilder noBounds() { return new IntRangeBuilderImpl(); }
    static IntRangeBuilder withBounds(IntRange.Bounds bounds) { return new IntRangeBuilderImpl(bounds); }
    static IntRangeBuilder withUnicodeBounds() { return new IntRangeBuilderImpl(IntRange.UNICODE_BOUNDS); }

    IntRangeBuilder add(int value);
    IntRangeBuilder addRange(int from, int to);
    IntRangeBuilder add(int... values);
    IntRangeBuilder addAll(Iterable<Integer> iter);
    IntRangeBuilder addAll(Iterator<Integer> iter);
    IntRangeBuilder remove(int value);
    IntRangeBuilder removeRange(int from, int to);
    IntRangeBuilder removeAll(Iterable<Integer> iter);
    IntRangeBuilder removeAll(Iterator<Integer> iter);

    IntRange build();
    IntRange build(Class<? extends IntRange> wantClass);

    SortedSet<Integer> underlyingSet();
}
