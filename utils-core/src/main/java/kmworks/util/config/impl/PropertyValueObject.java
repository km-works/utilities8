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

import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import kmworks.util.config.PropertyValue;
import kmworks.util.config.PropertyValueType;

/**
 *
 * @author cpl
 */
public class PropertyValueObject extends AbstractPropertyValue implements Map<String, PropertyValue>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final ImmutableMap<String, PropertyValue> value;
    
    public PropertyValueObject() {
        value = ImmutableMap.of();
    }
    
    public PropertyValueObject(@Nonnull final Map<String, Object> objMap) {
        ImmutableMap.Builder<String, PropertyValue> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<String, Object> entry : objMap.entrySet()) {
            Object entryVal = entry.getValue();
            PropertyValue propVal;
            if (entryVal == null) {
                propVal = PropertyValueFactory.mkNullValue();
            } else if (entryVal instanceof Boolean) {
                propVal = PropertyValueFactory.mkBooleanValue((boolean) entryVal);
            } else if (entryVal instanceof String) {
                propVal = PropertyValueFactory.mkStringValue((String) entryVal);
            } else {
                propVal = PropertyValueFactory.mkStringValue(entryVal.toString());
            }
            builder.put(entry.getKey(), propVal);
        }
        value = builder.build();
    }

    /* Implementation of interface PropertyValue */

    @Override
    public PropertyValueType valueType() {
        return PropertyValueType.OBJECT;
    }

    @Override
    public Map<String, Object> unwrapped() {
        ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<String, PropertyValue> entry : value.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().unwrapped());
        }
        return builder.build();
    }
    
    @Override
    public String render() {
        return "";  //TODO
    }
    
    /* Implementation of interface Map<String, PropertyValue> */
    
    @Override
    public PropertyValue get(@Nonnull final Object key) {
        return value.get(key);
    }
    
    @Override
    public boolean containsKey(@Nonnull final Object key) {
        return value.containsKey(key);
    }
    
    @Override
    public Set<String> keySet() {
        return value.keySet();
    }
    
    @Override
    public Set<Entry<String, PropertyValue>> entrySet() {
        return value.entrySet();
    }
    
    @Override
    public Collection<PropertyValue> values() {
        return value.values();
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }
    
    @Override
    public int size() {
        return value.size();
    }

    @Override
    public boolean containsValue(@Nonnull final Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyValue put(@Nonnull final String key, @Nonnull final PropertyValue value) {
        throw new UnsupportedOperationException("We are immutable.");
    }

    @Override
    public PropertyValue remove(@Nonnull final Object key) {
        throw new UnsupportedOperationException("We are immutable.");
    }

    @Override
    public void putAll(@Nonnull final Map<? extends String, ? extends PropertyValue> map) {
        throw new UnsupportedOperationException("We are immutable.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("We are immutable.");
    }

}
