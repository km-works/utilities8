package kmworks.util.config;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nonnull;
import kmworks.util.StringUtil;
import kmworks.util.config.impl.PropertyValueObject;

/**
 *
 * @author cpl
 */
public final class PropertyMap implements Serializable {
    
    private final PropertyValueObject properties;
    
    private PropertyMap() {
        properties = new PropertyValueObject();
    }
    
    private PropertyMap(@Nonnull final PropertyValueObject value) {
        this.properties = value;
    }
    
    private PropertyMap(@Nonnull final Iterable<Map.Entry<String, ?>> mapEntries) {
        final ImmutableMap.Builder<String,Object> builder = new ImmutableMap.Builder();
        for (Map.Entry<String, ?> kv : mapEntries) {
            if (kv != null && kv.getKey() != null) { // ignore null entries and entries with key == null
                builder.put(kv.getKey(), kv.getValue());
            }
        }
        properties = new PropertyValueObject(builder.build());
    }
    
    public static PropertyMap of(@Nonnull final Map<String, ?> map) {
        checkNotNull(map);
        return of((Iterable)map.entrySet());
    }

    public static PropertyMap of(@Nonnull final Iterable<Map.Entry<String, ?>> entrySet) {
        checkNotNull(entrySet);
        return new PropertyMap(entrySet);
    }
    
    public static PropertyMap of(@Nonnull final PropertyValueObject value) {
        checkNotNull(value);
        return new PropertyMap(value);
    }
    
    public static PropertyMap of() {
        return new PropertyMap();
    }
    
    public static Map.Entry<String, ?> kv(@Nonnull final String k, @Nonnull final Object v) {
        checkNotNull(k);
        checkNotNull(v);
        return new AbstractMap.SimpleImmutableEntry<>(k, v);
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PropertyMap))
            return false;
        final PropertyMap that = (PropertyMap) other;
        for (String key : properties.keySet()) {
            PropertyValue thisVal = getValue(key);
            PropertyValue thatVal = that.getValue(key);
            if (!thisVal.equals(thatVal))
                return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        return properties.hashCode();
    }
    
    @Override
    public String toString() {
        return properties.toString();
    }
    
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }
    
    public boolean isEmpty() {
        return properties.isEmpty();
    }
    
    public Set<String> keySet() {
        return properties.keySet();
    }
    
    public PropertyValue getValue(@Nonnull final String key) {
        checkNotNull(key);
        if (containsKey(key)) {
            return properties.get(key);
        } else {
            throw new NoSuchElementException("Missing key: " + key);
        }        
    }
    
    public <T> T get(@Nonnull final String key) {
        checkNotNull(key);
        return (T) getValue(key).unwrapped();
    }
    
    public <T> T getOrElse(@Nonnull final String key, final T defValue) {
        checkNotNull(key);
        if (containsKey(key)) {
            return get(key);
        } else {
            return defValue;
        }
    }
    
/*    
    public Integer getInteger(@Nonnull final String key) {
        final String value = get(key);
        if (value != null) {
            return StringUtil.Convert.toInteger(value);
        } else {
            return null;
        }
    }
    
    public Integer getIntegerOrElse(@Nonnull final String key, final Integer defValue) {
        final String value = get(key);
        if (value != null) {
            final Integer result = StringUtil.Convert.toInteger(value);
            return result != null ? result : defValue;
        } else {
            return defValue;
        }
    }
    
    public Integer getIntegerOrFail(@Nonnull final String key) {
        final String value = getOrFail(key);
        final Integer result = StringUtil.Convert.toInteger(value);
        if (result == null) {
            throw new RuntimeException("Cannot parse to integer: " + value);
        }
        return result;
    }
*/    
    
    public boolean getBoolean(@Nonnull final String key) {
        final Object value = get(key);
        if (value == null) {
            return false;
        } else if (value instanceof Boolean) {
            return (boolean) value;
        } else if (value instanceof String) {
            return StringUtil.Convert.toBoolean((String) value);
        } else {
            return false;
        }
    }

    public Map<String, Object> asMap() {
        return ImmutableMap.copyOf(properties.unwrapped());
    }
    
    public static PropertyMap merge(@Nonnull final PropertyMap head, @Nonnull final PropertyMap... tail) {
        checkNotNull(head);
        final ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder();
        builder.putAll(head.properties);
        for (PropertyMap prop : tail) {
            if (prop != null) {
                builder.putAll(prop.properties);
            }
        }
        return PropertyMap.of(builder.build());
    }
    
    public static class Builder {
        
        private final ImmutableMap.Builder<String, Object> builder = new ImmutableMap.Builder<>();
        
        public Builder put(String key, String value) {
            builder.put(key, value);
            return this;
        }
        
        public Builder putAll(Map<String, Object> map) {
            builder.putAll(map);
            return this;
        }
        
        public PropertyMap build() {
            return PropertyMap.of(builder.build());
        }
        
    }

}
