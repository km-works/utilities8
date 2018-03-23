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

import static com.google.common.base.Preconditions.checkNotNull;
import javax.annotation.Nonnull;

/**
 *
 * @author cpl
 */
public abstract class AbstractIntRange implements ClosedRange<Integer> {
    
    private final int first;
    private final int last;
    
    public AbstractIntRange(int a, int b) {
        this.first = a;
        this.last = b;
    }

    @Override
    public Integer first() {
        return first;
    }

    @Override
    public Integer last() {
        return last;
    }

    @Override
    public int size() {
        return last() - first() + 1;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @Nonnull
    public Integer distance(@Nonnull final Integer a, @Nonnull final Integer b) {
        checkNotNull(a);
        checkNotNull(b);
        return Math.abs(a - b);
    }
    
    public abstract Integer indexOf(int member);

}
