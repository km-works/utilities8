/*
 *  Copyright (C) 2005-2017 Christian P. Lerch, Vienna, Austria.
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
 *  along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;
import kmworks.util.lambda.Function0;

import java.util.SortedSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static kmworks.util.StringPool.MUST_NOT_BE_EMPTY_MSG;
import static kmworks.util.StringPool.MUST_NOT_BE_NULL_MSG;
import static kmworks.util.StringPool.OUT_OF_RANGE_MSG;

/**
 * Non-empty, not necessarily contiguous, closed range of integers.
 *
 * @author cpl
 */
abstract class AbstractIntRange implements IntRange {

    private final int first;
    private final int last;
    private final int size;
    private final Function0<?> initializer;


    AbstractIntRange(Initializer init) {
        this.first = init.getFirst();
        this.last = init.getLast();
        this.size = init.getSize();
        this.initializer = init.getInitializer();
    }


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


    protected static int countMembersBetween(IntRange range, int first, int last) {
        int count = 0;
        for (int value = first; value <= last; value++) {
            if (range.contains(value)) {
                count++;
            }
        }
        return count;
    }

    protected static Integer firstMemberAtOrAbove(IntRange range, int value) {
        if (value < range.first() || value > range.last()) {
            throw new IllegalArgumentException("value " + OUT_OF_RANGE_MSG);
        }
        while (value <= range.last() && !range.contains(value)) {
            value++;
        }
        return value <= range.last() ? value : null;
    }

    protected static Integer firstMemberBelow(IntRange range, int value) {
        if (value < range.first() || value > range.last()) {
            throw new IllegalArgumentException("value " + OUT_OF_RANGE_MSG);
        }
        do {
            value--;
        } while (value >= range.first() && !range.contains(value));
        return value >= range.first() ? value : null;
    }

    protected static SortedSet<Integer> checkNotNullOrEmpty(SortedSet<Integer> set) {
        checkNotNull(set, "sortedSet " + MUST_NOT_BE_NULL_MSG);
        checkArgument(!set.isEmpty(), "sortedSet " + MUST_NOT_BE_EMPTY_MSG);
        return set;
    }

    interface Initializer {

        int getFirst();

        int getLast();

        int getSize();

        default Function0<?> getInitializer() {
            return () -> null;
        }

    }

}
