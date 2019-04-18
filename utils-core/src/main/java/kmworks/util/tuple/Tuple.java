/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmworks.util.tuple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author cpl
 */
public abstract class Tuple {
    
    public abstract int size();
    public abstract int index(String typeName);
    public abstract String name(int i);
    public abstract <T> T value(int i);
    public abstract Class<?> type(int i);
    public abstract Class<?> type(String name);
    
    @SuppressWarnings("unchecked")
    public <T> T value(String typeName) {
        return (T) value(index(typeName));
    }
    
    public List<String> names() {
        final ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
        for (int i=0; i<size(); i++) {
            builder.add(name(i));
        }
        return builder.build();
    }

    public List<Object> values() {
        final ImmutableList.Builder<Object> builder = new ImmutableList.Builder<>();
        for (int i=0; i<size(); i++) {
            builder.add(value(i));
        }
        return builder.build();
    }

    public Map<String, Object> asMap() {
        final ImmutableMap.Builder builder = new ImmutableMap.Builder<>();
        for (int i=0; i<size(); i++) {
            builder.put(name(i), value(i));
        }
        return builder.build();
    }

    public static Tuple of() {
        return builder().setValues().build();
    }
    
    public static Tuple of(Object... values) {
        List<Class<?>> types = Arrays.asList(values).stream()
                .map(v -> v == null ? Object.class : v.getClass())
                .collect(Collectors.toList());
        return new TupleImpl.Builder().setTypes(types)
                .setValues(values)
                .build();
    }

    public static Builder builder(Class<?>... types) {    
        return new TupleImpl.Builder().setTypes(Arrays.asList(types));
    }
    
    public abstract static class Builder {
        
        Builder() {}
        
        public abstract Builder setTypes(List<Class<?>> types);
        public abstract Builder setValues(Object... values);
        public abstract Builder setNames(String... names);
        public abstract Tuple build();
    }
    
}
