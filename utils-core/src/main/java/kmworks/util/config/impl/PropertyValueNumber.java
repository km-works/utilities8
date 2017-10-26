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
import kmworks.util.config.PropertyValueType;


/**
 *
 * @author cpl
 */
public final class PropertyValueNumber extends AbstractPropertyValue implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final Number value;
    private final boolean isDecimal;
    
    protected PropertyValueNumber(double value) {
        this.value = value;
        this.isDecimal = true;
    }

    protected PropertyValueNumber(long value) {
        this.value = value;
        this.isDecimal = false;
    }

    @Override
    public PropertyValueType valueType() {
        return PropertyValueType.NUMBER;
    }

    @Override
    public Number unwrapped() {
        return value;
    }

    @Override
    public String render() {
        return value.toString();
    }
    
    public double asDouble() {
        return isDecimal ? (Double) value : value.doubleValue();
    }
    
    public long asLong() {
        return isDecimal ? value.longValue() : (Long) value;
    }
    
}
