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

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import kmworks.util.config.PropertyValue;
import kmworks.util.config.PropertyValueType;

/**
 *
 * @author cpl
 */
public class PropertyValueList extends AbstractPropertyValue implements Iterable<PropertyValue>, Serializable {
    
    private static final long serialVersionUID = 1L;

    private final ImmutableList<PropertyValue> value;
    
    protected PropertyValueList(Iterator<PropertyValue> iter) {
        ImmutableList.Builder<PropertyValue> builder = new ImmutableList.Builder<>();
        builder.addAll(iter);
        this.value = builder.build();
    }

    @Override
    public PropertyValueType valueType() {
        return PropertyValueType.LIST;
    }

    @Override
    public List<Object> unwrapped() {
        ImmutableList.Builder<Object> builder = new ImmutableList.Builder<>();
        for (PropertyValue item : value) {
            builder.add(item.unwrapped());
        }
        return builder.build();
    }

    @Override
    public String render() {
        final StringBuilder sb = new StringBuilder();
        sb.append('[');
        final Iterator<PropertyValue> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next().render());
            if (it.hasNext()) {
                sb.append(',');
            }
        }
        sb.append(']');
        return sb.toString();
    }

    @Override
    public Iterator<PropertyValue> iterator() {
        return value.listIterator();
    }
    
}
