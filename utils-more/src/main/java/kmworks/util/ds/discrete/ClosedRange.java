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

/**
 *
 * @author cpl
 * @param <T>
 */
public interface ClosedRange<T extends Comparable<T>> extends Comparable<ClosedRange<T>>, MetricSpace<T, Integer> {
    
    T first();
    T last();
    
    default boolean contains(T value) {
        return value.compareTo(first()) >= 0 && value.compareTo(last()) <= 0;
    }
    
    default int size() {
        return isEmpty() ? 0 : distance(first(), last()) + 1;
    }
    
    default boolean isEmpty() {
        return first() == null && last() == null;
    }
    
//    default boolean overlaps(ClosedRange<T> range) {
//        return contains(range.first()) || contains(range.last());
//    }
//    

    @Override
    default int compareTo(ClosedRange<T> other) {
        return first().compareTo(other.first());
    }
    
}
