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
 *  along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.ds.rng.impl;

import static com.google.common.base.Preconditions.*;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import kmworks.util.ds.rng.IntRange;
import kmworks.util.lambda.Function0;

import javax.annotation.Nonnull;

/**
 * A non-overlaping sequence if SimpleIntRange instances.
 *
 * @author cpl
 */
public final class IntRangeSeq extends AbstractIntRange {

    private List<Entry> entries;

    IntRangeSeq(List<IntRange> segments) {
        this(segments.get(0).first(), segments.get(segments.size() - 1).last(), segments);
    }

    @SuppressWarnings("unchecked")
    IntRangeSeq(int first, int last, @Nonnull final Iterable<IntRange> segments) {
        super(new AbstractIntRange.Initializer() {
            private final ImmutableList.Builder<Entry> entryBuilder = new ImmutableList.Builder<>();

            @Override
            public int getFirst() {
                return first;
            }

            @Override
            public int getLast() {
                return last;
            }

            @Override
            public int getSize() {
                int offset = 0, size = 0;
                for (IntRange range : segments) {
                    entryBuilder.add(new Entry(range, offset));
                    offset += range.span();
                    size += range.size();
                }
                return size;
            }

            @Override
            public Function0<List<Entry>> getInitializer() {
                return () -> entryBuilder.build();
            }
        });
        entries = (List<Entry>) initializer().apply();
    }


    @Override
    public boolean contains(int value) {
        checkNotNull(value);
        return indexOf(value) != null;
    }

    @Override
    public PeekingIterator<Integer> iterator() {
        return (PeekingIterator<Integer>) Iterators.concat(entries.stream()
                .map(e -> e.range.iterator())
                .collect(Collectors.toList()).iterator());
    }

    private Integer indexOf(int value) {
        int lower = 0, upper = entries.size() - 1;
        // do binary search
        while (true) {
            if (upper < lower) {
                return null;
            }
            int curr = lower + (upper - lower) / 2;
            Entry entry = entries.get(curr);
            IntRange range = entry.range;
            if (range.contains(value)) {
                // guess.first() <= value <= guess.last()
                return value - range.first() + entry.offset;
            } else if (value > range.last()) {
                if (curr + 1 < entries.size() && value < entries.get(curr + 1).range.first()) {
                    return null;
                }
                lower = curr + 1;
            } else {
                // value < guess.first()
                if (curr - 1 >= 0 && value > entries.get(curr - 1).range.last()) {
                    return null;
                }
                upper = curr - 1;
            }
        }
    }


    private static class Entry {

        private final IntRange range;
        private final int offset;

        Entry(IntRange range, int offset) {
            this.range = range;
            this.offset = offset;
        }

    }

    public static class Builder {

        private DiscontiguousIntRange.Builder memberBuilder;

        public Builder() {
            memberBuilder = new DiscontiguousIntRange.Builder();
        }

        public Builder add(int value) {
            memberBuilder.add(value);
            return this;
        }

        public Builder add(IntRange range) {
            memberBuilder.addRange(range.first(), range.last());
            return this;
        }

        public Builder addRange(int first, int last) {
            memberBuilder.addRange(first, last);
            return this;
        }

        public Builder add(int... values) {
            for (int value : values) {
                memberBuilder.add(value);
            }
            return this;
        }

        public Builder addIntegers(Iterable<Integer> iter) {
            for (Integer value : iter) {
                if (value != null) {
                    memberBuilder.add(value);
                }
            }
            return this;
        }

        public Builder addRanges(Iterable<IntRange> iter) {
            for (IntRange range : iter) {
                if (range != null) {
                    add(range);
                }
            }
            return this;
        }

        public List<IntRange> build() {
            ImmutableList.Builder<IntRange> listBuilder = new ImmutableList.Builder<>();
            IntRange range = memberBuilder.build();
            int curr = range.first();
            while (true) {
                int first = curr;
                while (range.contains(curr) && curr <= range.last()) {
                    curr += 1;
                }
                listBuilder.add(new ContiguousIntRange(first, curr - 1));
                while (curr <= range.last() && !range.contains(curr)) {
                    curr += 1;
                }
                if (curr > range.last()) break;
            }
            return listBuilder.build();
        }

    }

}
