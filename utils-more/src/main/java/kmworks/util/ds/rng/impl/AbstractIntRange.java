/*
 *  Copyright (C) 2005-2018 Christian P. Lerch, Vienna, Austria.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 3 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along withBounds this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.ds.rng.impl;

import com.google.common.collect.ImmutableSortedSet;
import kmworks.util.ObjectUtil;
import kmworks.util.base.Equals;
//import kmworks.util.collect.ImmutableSortedSet;
import kmworks.util.ds.rng.IntRange;
import kmworks.util.lambda.Function0;

import java.util.Iterator;
import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkArgument;
import static kmworks.util.StringPool.OUT_OF_RANGE_MSG;

/**
 * Non-empty, not necessarily contiguous, closed range of integers.
 *
 * @author cpl
 */
abstract class AbstractIntRange implements IntRange, Equals {

    private final Bounds bounds;
    private final int first;
    private final int last;
    private final int size;
    private final Function0<?> initializer;


    AbstractIntRange(Initializer initializer) {
        this.bounds = initializer.getBounds();
        this.first = checkBounds(initializer.getFirst());
        this.last = checkBounds(initializer.getLast());
        this.size = initializer.getSize();
        this.initializer = initializer.get();
    }

    @Override
    public final Bounds bounds() { return bounds; }

    @Override
    public final int first() {
        return first;
    }

    @Override
    public final int last() {
        return last;
    }

    @Override
    public final int size() {
        return size;
    }

    public final Function0<?> initializer() {
        return initializer;
    }

    @Override
    public boolean canEqualWith(Object obj) {
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public SortedSet<Integer> asSet() {
        return new ImmutableSortedSet.Builder<Integer>(IntRange.COMPARATOR).addAll(iterator()).build();
    }

    int checkBounds(Integer value) {
        return IntRange.checkBounds(value, bounds);
    }


    int hash(Iterator<Integer> iter) {
        int result = 1;
        while (iter.hasNext()) {
            Integer value = iter.next();
            int key = (value == null) ? 0 : ObjectUtil.hash(value);
            result = (result * ObjectUtil.KNUTH_CONST) ^ key;
        }
        return result;
    }

    protected static int countMembersBetween(IntRange range, int from, int to) {
        int count = 0;
        for (int value = from; value <= to; value++) {
            if (range.contains(value)) {
                count++;
            }
        }
        return count;
    }

    protected static Integer firstMemberAtOrAbove(IntRange range, int value) {
        checkArgument(value < range.first() || value > range.last(),
                "value " + OUT_OF_RANGE_MSG);
        while (value <= range.last() && !range.contains(value)) {
            value++;
        }
        return value <= range.last() ? value : null;
    }

    protected static Integer firstMemberBelow(IntRange range, int value) {
        checkArgument(value < range.first() || value > range.last(),
                "value " + OUT_OF_RANGE_MSG);
        do {
            value--;
        } while (value >= range.first() && !range.contains(value));
        return value >= range.first() ? value : null;
    }

    interface Initializer {
        Bounds getBounds();
        int getFirst();
        int getLast();
        int getSize();
        default Function0<?> get() {
            return () -> null;
        }
    }

}
