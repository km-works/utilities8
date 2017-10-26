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
package kmworks.util.config.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import kmworks.util.config.PropertyValue;

/**
 *
 * @author cpl
 */
public abstract class AbstractPropertyValue implements PropertyValue {

    @Override
    public int hashCode() {
        return unwrapped().hashCode();
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PropertyValue)) {
            return false;
        } else {
            final PropertyValue other = (PropertyValue) obj;
            return this.valueType() == other.valueType()
                    && this.unwrapped().equals(other.unwrapped());
        }
    }

    @Override @Nonnull
    public String toString() {
        return getClass().getSimpleName() + "(" + unwrapped() + ")";
    }

}
