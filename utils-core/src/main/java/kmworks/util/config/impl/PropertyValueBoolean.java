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
import javax.annotation.Nonnull;
import kmworks.util.config.PropertyValueType;

/**
 *
 * @author cpl
 */
public final class PropertyValueBoolean extends AbstractPropertyValue implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    protected static final PropertyValueBoolean TRUE_Value = new PropertyValueBoolean(true);
    protected static final PropertyValueBoolean FALSE_Value = new PropertyValueBoolean(false);

    private final boolean value;

    private PropertyValueBoolean(boolean value) {
        this.value = value;
    }

    @Override @Nonnull
    public PropertyValueType valueType() {
        return PropertyValueType.BOOLEAN;
    }

    @Override @Nonnull
    public Boolean unwrapped() {
        return value;
    }

    @Override @Nonnull
    public String render() {
        return value ? "true" : "false";
    }
    
}
