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

/**
 *
 * @author cpl
 */
public class IntRange extends AbstractIntRange {
    
    public IntRange(int first, int last) {
        super(first, last);
    }
    
    @Override
    public boolean contains(Integer member) {
        checkNotNull(member);
        return member >= first() && member <= last();
    }
    
    @Override
    public Integer indexOf(int member) {
        return contains(member) ? member - first() : null;
    }
    
}
