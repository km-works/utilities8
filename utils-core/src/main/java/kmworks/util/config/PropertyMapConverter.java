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

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Nonnull;

/**
 *
 * @author cpl
 */
public final class PropertyMapConverter {

    private PropertyMapConverter() {
    }

    public static PropertyMap fromProperties(@Nonnull final Properties properties) {
        checkNotNull(properties);
        ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            builder.put(entry.getKey().toString(), entry.getValue());
        }
        return PropertyMap.of(builder.build());
    }

    public static Properties toProperties(@Nonnull final PropertyMap propertyMap) {
        checkNotNull(propertyMap);
        Properties result = new Properties();
        for (String key : propertyMap.keySet()) {
            result.put(key, propertyMap.get(key));
        }
        return result;
    }
    
    public static PropertyMap fromConfig(@Nonnull final Config config) {
        checkNotNull(config);
        ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
            builder.put(entry.getKey(), entry.getValue().unwrapped());
        }
        return PropertyMap.of(builder.build());
    }
    
    public static Config toConfig(@Nonnull final PropertyMap propertyMap) {
        checkNotNull(propertyMap);
        return ConfigFactory.parseMap(propertyMap.asMap());
    }
}
