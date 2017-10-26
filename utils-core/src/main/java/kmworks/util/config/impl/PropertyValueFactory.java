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

import static com.google.common.base.Preconditions.*;
import java.util.Iterator;
import javax.annotation.Nonnull;
import kmworks.util.config.PropertyValue;

/**
 *
 * @author cpl
 */
public final class PropertyValueFactory {
    
    private PropertyValueFactory() {}
    
    public static PropertyValueNull mkNullValue() {
        return PropertyValueNull.NULL_Value;
    }
    
    public static PropertyValueBoolean mkTrueValue() {
        return PropertyValueBoolean.TRUE_Value;
    }
    
    public static PropertyValueBoolean mkFalseValue() {
        return PropertyValueBoolean.FALSE_Value;
    }
    
    public static PropertyValueBoolean mkBooleanValue(boolean bool) {
        return bool ? mkTrueValue() : mkFalseValue();
    }
    
    public static PropertyValueString mkStringValue(@Nonnull final CharSequence cs) {
        checkNotNull(cs);
        return new PropertyValueString(cs);
    }
    
    public static PropertyValueNumber mkNumberValue(double number) {
        return new PropertyValueNumber(number);
    }
    
    public static PropertyValueNumber mkNumberValue(long number) {
        return new PropertyValueNumber(number);
    }
    
    public static PropertyValueList mkListValue(@Nonnull final Iterator<PropertyValue> iterator) {
        checkNotNull(iterator);
        return new PropertyValueList(iterator);
    }
    
    public static PropertyValueList mkListValue(@Nonnull final Iterable<PropertyValue> iterable) {
        checkNotNull(iterable);
        return new PropertyValueList(iterable.iterator());
    }
}
