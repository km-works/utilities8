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

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import kmworks.util.ObjectUtil;

/**
 *
 * @author cpl
 */
public final class CompactIntRange extends AbstractIntRange {

    CompactIntRange(Integer first, Integer last) {
        super(new AbstractIntRange.Initializer() {
            @Override public int getFirst() { return first; }
            @Override public int getLast() { return last; }
            @Override public int getSize() { return last - first + 1; }
        });
    }

    @Override
    public boolean contains(int value) {
        return value >= first() && value <= last();
    }

    @Override
    public PeekingIterator<Integer> iterator() {

        return Iterators.peekingIterator(new AbstractIterator<Integer>() {
            int nextMember = first() - 1;

            @Override
            protected Integer computeNext() {
                nextMember += 1;
                return nextMember > last() ? endOfData() : nextMember;
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!canEqualWith(obj)) {
            return false;
        }
        final CompactIntRange other = (CompactIntRange) obj;
        return this.size() == other.size()
                && this.first() == other.first()
                && this.last() == other.last();
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hash(size(), first(), last());
    }


    /*
    @Override
    public Integer indexOf(int member) {
        return contains(member) ? member - first() : null;
    }
    */
    
}
