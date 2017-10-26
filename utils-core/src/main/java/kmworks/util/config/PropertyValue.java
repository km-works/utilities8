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
package kmworks.util.config;

import javax.annotation.Nonnull;
import kmworks.util.config.impl.PropertyValueFactory;

/**
 *
 * @author cpl
 */
public interface PropertyValue {
    
    @Nonnull
    PropertyValueType valueType();
    
    @Nonnull
    Object unwrapped();
    
    @Nonnull
    String render();
    
    @Nonnull
    static PropertyValue NULL() {
        return PropertyValueFactory.mkNullValue();
    }
    
    @Nonnull
    static PropertyValue TRUE() {
        return PropertyValueFactory.mkTrueValue();
    }
    
    @Nonnull
    static PropertyValue FALSE() {
        return PropertyValueFactory.mkFalseValue();
    }
    
    @Nonnull
    static PropertyValue STRING(@Nonnull final String s) {
        return PropertyValueFactory.mkStringValue(s);
    }
    
    @Nonnull
    static PropertyValue NUMBER(double n) {
        return PropertyValueFactory.mkNumberValue(n);
    }
    
    @Nonnull
    static PropertyValue NUMBER(long n) {
        return PropertyValueFactory.mkNumberValue(n);
    }
    
}
