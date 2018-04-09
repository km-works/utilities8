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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;

import static kmworks.util.base.Preconditions.*;
import kmworks.util.ds.rng.IntRange;
import kmworks.util.lambda.Function0;

import javax.annotation.Nonnull;

/**
 * A not empty, segmented IntRange aggregated from a non-overlapping, ordered sequence of IntRange instances.
 *
 * @author cpl
 */
public final class SegmentedIntRange extends AbstractIntRange {

    private List<Segment> segments;

    /**
     * Construct a minimally SegmentedIntRange from an unordered list of possibly overlapping IntRange pieces.
     * After sorting the given list of IntRange pieces, successive, overlapping pieces will be consolidated into
     * a single constituting segment, while non-overlapping pieces will be added unmodified.
     * @param pieces unordered list of possibly overlapping IntRange pieces
     */
    @SuppressWarnings("unchecked")
    SegmentedIntRange(@Nonnull final List<IntRange> pieces) {

        super(new AbstractIntRange.Initializer() {
            private @Nonnull final List<IntRange> orderedPieces =
                    checkNotEmpty(Lists.newArrayList(new TreeSet<IntRange>(checkNotNull(pieces))));
            private final ImmutableList.Builder<Segment> elementsBuilder = new ImmutableList.Builder<>();

            @Override
            public int getFirst() {
                return orderedPieces.get(0).first();
            }

            @Override
            public int getLast() {
                return orderedPieces.get(orderedPieces.size() - 1).last();
            }

            @Override
            public int getSize() {
                int offset = 0, size = 0;
                IntRange lastPiece = orderedPieces.get(0);
                for (int i=1; i<orderedPieces.size(); i++) {
                    final IntRange currPiece = orderedPieces.get(i);
                    if (currPiece.overlaps(lastPiece)) {
                        // lastPiece = union(lastPiece, currPiece)
                    } else {
                        elementsBuilder.add(new Segment(lastPiece, offset));
                        offset += lastPiece.span();
                        size += lastPiece.size();
                        lastPiece = currPiece;
                    }
                }
                elementsBuilder.add(new Segment(lastPiece, offset));
                return size + lastPiece.size();
            }

            @Override
            public Function0<?> get() {
                return elementsBuilder::build;
            }
        });

        segments = (List<Segment>) initializer().apply();
    }

    /**
     * Construct a maximally SegmentedIntRange from a sorted set of integers.
     * Each compact subset will be mapped to a constituting IntRange segment.
     * @param sortedSet sorted set of integers
     */
    @SuppressWarnings("unchecked")
    SegmentedIntRange(@Nonnull final SortedSet<Integer> sortedSet) {

        super(new AbstractIntRange.Initializer() {
            private final ImmutableList.Builder<Segment> elementsBuilder = new ImmutableList.Builder<>();

            @Override
            public int getFirst() {
                return sortedSet.first();
            }

            @Override
            public int getLast() {
                return sortedSet.last();
            }

            @Override
            public int getSize() {
                return sortedSet.size();
            }

            @Override
            public Function0<?> get() {
                int curr = sortedSet.first(), offset = 0;

                do {
                    int first = curr;
                    while (sortedSet.contains(curr) && curr <= sortedSet.last()) {
                        curr += 1;
                    }

                    final IntRange range = new CompactIntRange(first, curr - 1);
                    elementsBuilder.add(new Segment(range, 0));
                    offset += range.span();

                    while (curr <= sortedSet.last() && !sortedSet.contains(curr)) {
                        curr += 1;
                    }
                } while (curr <= sortedSet.last());

                return elementsBuilder::build;
            }
        });

        segments = (List<Segment>) initializer().apply();
    }


    @Override
    public boolean contains(int value) {
        return indexOf(value) != null;
    }

    @Override
    public PeekingIterator<Integer> iterator() {
        return (PeekingIterator<Integer>) Iterators.concat(segments.stream()
                .map(e -> e.range.iterator())
                .collect(Collectors.toList()).iterator());
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!canEqualWith(obj)) {
            return false;
        }
        final SegmentedIntRange other = (SegmentedIntRange) obj;
        return this.first() == other.first()        // checked for optimization purposes only
                && this.last() == other.last()      // ditto
                && memberEquals(this, other);
    }

    @Override
    public int hashCode() {
        // no need to hash size, first and last here because those are not independant values
        return hash(this.iterator());
    }


    private Integer indexOf(int value) {
        int lower = 0, upper = segments.size() - 1;
        // do binary search
        while (true) {
            if (upper < lower) {
                return null;
            }
            int curr = lower + (upper - lower) / 2;
            Segment entry = segments.get(curr);
            IntRange range = entry.range;
            if (range.contains(value)) {
                // guess.first() <= value <= guess.last()
                return value - range.first() + entry.offset;
            } else if (value > range.last()) {
                if (curr + 1 < segments.size() && value < segments.get(curr + 1).range.first()) {
                    return null;
                }
                lower = curr + 1;
            } else {
                // value < guess.first()
                if (curr - 1 >= 0 && value > segments.get(curr - 1).range.last()) {
                    return null;
                }
                upper = curr - 1;
            }
        }
    }


    private static class Segment {
        private final IntRange range;
        private final int offset;

        Segment(IntRange range, int offset) {
            this.range = range;
            this.offset = offset;
        }
    }

    public static class Builder {
        private BitsetIntRange.Builder memberBuilder;

        public Builder() {
            memberBuilder = new BitsetIntRange.Builder();
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
            do {
                int first = curr;
                while (range.contains(curr) && curr <= range.last()) {
                    curr += 1;
                }
                listBuilder.add(new CompactIntRange(first, curr - 1));
                while (curr <= range.last() && !range.contains(curr)) {
                    curr += 1;
                }
            } while (curr <= range.last());
            return listBuilder.build();
        }

    }

}
