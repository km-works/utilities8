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
package kmworks.util.ds.discrete;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.ImmutableList;
import java.util.List;
import kmworks.util.cp.CodepointBitSet;
import kmworks.util.cp.CodepointSet;

/**
 *
 * @author cpl
 */
public final class DiscontiguousIntRange extends AbstractIntRange {
    
    private final List<Entry> entries;
    
    public static DiscontiguousIntRange of(List<IntRange> segments) {
        return new DiscontiguousIntRange(segments);
    }
    
    private DiscontiguousIntRange(List<IntRange> segments) {
        this(segments.get(0).first(), segments.get(segments.size()-1).last(), segments);
    }
    
    private DiscontiguousIntRange(int first, int last, Iterable<IntRange> segments) {
        super(first, last);
        List<IntRange> ranges = new Builder().addRanges(segments).build();
        ImmutableList.Builder<Entry> listBuilder = new ImmutableList.Builder<>();
        int offset = 0;
        for (IntRange range : ranges) {
            listBuilder.add(new Entry(range, offset));
            int size = range.size();
            offset += size;
        }
        entries = listBuilder.build();
    }
    
    @Override
    public boolean contains(Integer member) {
        checkNotNull(member);
        return indexOf(member) != null;
    }
    
    @Override
    public Integer indexOf(int member) {
        int lower = 0, upper = entries.size() - 1;
        while (true) {
            if (upper < lower) {
                return null;
            }
            int curr = lower + (upper - lower) / 2;
            Entry entry = entries.get(curr);
            IntRange range = entry.range;
            if (range.contains(member)) {
                /* guess.first() <= member <= guess.last() */
                return member - range.first() + entry.offset;
            } else if (member > range.last()) {
                if (curr + 1 < entries.size() && member < entries.get(curr + 1).range.first()) {
                    return null;
                }
                lower = curr + 1;
            } else {
                /* member < guess.first() */
                if (curr - 1 >= 0 && member > entries.get(curr - 1).range.last()) {
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
        
        private CodepointBitSet.Builder memberBuilder;
        
        public Builder() {
            memberBuilder = new CodepointBitSet.Builder();
        }
        
        public Builder add(int member) {
            memberBuilder.add(member);
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

        public Builder add(int... args) {
            for (int member : args) {
                memberBuilder.add(member);
            }
            return this;
        }

        public Builder addIntegers(Iterable<Integer> iter) {
            for (Integer member : iter) {
                if (member != null) {
                    memberBuilder.add(member);
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
            CodepointSet cps = memberBuilder.build();
            int curr = cps.first();
            while (true) {
                int first = curr;
                while (cps.contains(curr) && curr <= cps.last()) {
                    curr += 1;
                }
                listBuilder.add(new IntRange(first, curr-1));
                while (curr <= cps.last() && !cps.contains(curr)) {
                    curr += 1;
                }
                if (curr > cps.last()) break;
            }
            return listBuilder.build();
        }

    }
    
}
