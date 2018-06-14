package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;
import kmworks.util.ds.rng.IntRangeBuilder;
import kmworks.util.ds.rng.IntRangeFactory;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntRangeBuilderImpl implements IntRangeBuilder {

    private SortedSet<Integer> set = new TreeSet();

    private IntRange.Bounds bounds;

    public IntRangeBuilderImpl() { this(IntRange.NO_BOUNDS); }
    public IntRangeBuilderImpl(IntRange.Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public IntRangeBuilder add(int value) {
        set.add(checkBounds(value));
        return this;
    }

    @Override
    public IntRangeBuilder addRange(int from, int to) {
        for (int value = from; value <= to; value++) {
            add(checkBounds(value));
        }
        return this;
    }

    @Override
    public IntRangeBuilder add(int... values) {
        for (int value : values) {
            add(checkBounds(value));
        }
        return this;
    }

    @Override
    public IntRangeBuilder addAll(Iterable<Integer> iter) {
        return addAll(iter.iterator());
    }

    @Override
    public IntRangeBuilder addAll(Iterator<Integer> iter) {
        while (iter.hasNext()) {
            add(checkBounds(iter.next()));
        }
        return this;
    }

    @Override
    public IntRangeBuilder remove(int value) {
        set.remove(checkBounds(value));
        return this;
    }

    @Override
    public IntRangeBuilder removeRange(int from, int to) {
        for (int value = from; value <= to; value++) {
            remove(checkBounds(value));
        }
        return this;
    }

    @Override
    public IntRangeBuilder removeAll(Iterable<Integer> iter) {
        return removeAll(iter.iterator());
    }

    @Override
    public IntRangeBuilder removeAll(Iterator<Integer> iter) {
        while (iter.hasNext()) {
            remove(checkBounds(iter.next()));
        }
        return this;
    }

    @Override
    public IntRange build() {
        int span = set.last() - set.first() + 1;
        if (set.size() == span) {
            // if range is compact
            return IntRangeFactory.withBounds(bounds).createCompactIntRange(set.first(), set.last());
        } else if (countSegments() <= 8) {
            // if low number of segments (= fast access)
            return IntRangeFactory.withBounds(bounds).createSegmentedIntRange(set);
        } else {
            // otherwise use BitsetIntRange
            return IntRangeFactory.withBounds(bounds).createBitsetIntRange(set);
        }
    }

    @Override
    public IntRange build(Class<? extends IntRange> wantClass) {
        int span = set.last() - set.first() + 1;
        if (set.size() == span && wantClass == CompactIntRange.class) {
            // use CompactIntRange only if range is indeed compact
            return IntRangeFactory.withBounds(bounds).createCompactIntRange(set.first(), set.last());
        } else if (wantClass == SegmentedIntRange.class) {
            // use SegementedIntRange as requested
            return IntRangeFactory.withBounds(bounds).createSegmentedIntRange(set);
        } else {
            // otherwise use BitsetIntRange
            return IntRangeFactory.withBounds(bounds).createBitsetIntRange(set);
        }
    }

    @Override
    public SortedSet<Integer> underlyingSet() {
        return set;
    }

    private int checkBounds(Integer value) {
        return IntRange.checkBounds(value, bounds);
    }

    private int countSegments() {
        int count = 0, curr = set.first();
        do {
            while (set.contains(curr) && curr <= set.last()) {
                curr += 1;
            }
            count += 1;
            while (curr <= set.last() && !set.contains(curr)) {
                curr += 1;
            }
        } while (curr <= set.last());
        return count;
    }

}
