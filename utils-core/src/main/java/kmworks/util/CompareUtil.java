/*
 * Copyright (C) 2005-2016 Christian P. Lerch <christian.p.lerch[at]gmail.com>
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util;

import java.io.Serializable;
import java.util.Comparator;
import kmworks.util.base.TotalOrder;

/**
 *
 * @author Christian P. Lerch
 */
public final class CompareUtil {

    private CompareUtil() {
    }

    public static <T1 extends Comparable<? super T1>, T2 extends Comparable<? super T2>>
            int compare(T1 x1, T1 y1, T2 x2, T2 y2) {
        int result = x1.compareTo(y1);
        if (result == 0) {
            result = x2.compareTo(y2);
        }
        return result;
    }

    public static <T1, T2> int compare(Comparator<? super T1> comp1, T1 x1, T1 y1,
            Comparator<? super T2> comp2, T2 x2, T2 y2) {
        int result = comp1.compare(x1, y1);
        if (result == 0) {
            result = comp2.compare(x2, y2);
        }
        return result;
    }

    /**
     * Get a TotalOrder based on the natural (compareTo-based) order associated with the given type.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> TotalOrder<T> naturalOrder() {
        return (TotalOrder<T>) NaturalOrder.INSTANCE;
    }

    private static final class NaturalOrder<T extends Comparable<? super T>>
            extends TotalOrder<T> implements Serializable {

        private final static long serialVersionUID = 1L;
        private static final NaturalOrder<Comparable<Object>> INSTANCE = new NaturalOrder<Comparable<Object>>();

        private NaturalOrder() {
        }

        public int compare(T arg1, T arg2) {
            return arg1.compareTo(arg2);
        }
    }

    /**
     * Wrap a Comparator as a TotalOrder.
     */
    public static <T> TotalOrder<T> asTotalOrder(Comparator<? super T> comp) {
        return new ComparatorTotalOrder<>(comp);
    }

    private static final class ComparatorTotalOrder<T> extends TotalOrder<T> {

        private final static long serialVersionUID = 1L;
        private final Comparator<? super T> _comp;

        public ComparatorTotalOrder(Comparator<? super T> comp) {
            _comp = comp;
        }

        public int compare(T arg1, T arg2) {
            return _comp.compare(arg1, arg2);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof ComparatorTotalOrder<?>)) {
                return false;
            } else {
                return _comp.equals(((ComparatorTotalOrder<?>) o)._comp);
            }
        }

        public int hashCode() {
            return ObjectUtil.hash(ComparatorTotalOrder.class, _comp);
        }
    }
}
