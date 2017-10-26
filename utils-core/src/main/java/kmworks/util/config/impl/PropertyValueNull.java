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

import java.io.Serializable;
import kmworks.util.config.PropertyValue;
import kmworks.util.config.PropertyValueType;

/**
 *
 * @author cpl
 */
public final class PropertyValueNull extends AbstractPropertyValue implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected static final PropertyValueNull NULL_Value = new PropertyValueNull();
    
    private PropertyValueNull() {}

    @Override
    public PropertyValueType valueType() {
        return PropertyValueType.NULL;
    }

    @Override
    public Object unwrapped() {
        return null;
    }
    
    @Override
    public String render() {
        return "null";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PropertyValue)) {
            return false;
        } else {
            final PropertyValue other = (PropertyValue) obj;
            return this.valueType() == other.valueType()
                    && null == other.unwrapped();
        }
    }

}
