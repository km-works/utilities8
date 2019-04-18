/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmworks.util.tuple;

import static com.google.common.base.Preconditions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author cpl
 */
public final class TupleImpl extends Tuple {
    
    @Nonnull
    private final List<Class<?>> types;
    @Nonnull
    private final List<Object> values;
    @Nonnull
    private final List<String> names;
    @Nonnull
    private final Map<String, Integer> nameMap;
    
    public TupleImpl(
            @Nonnull List<Class<?>> types,
            @Nonnull List<Object> values,
            @Nullable List<String> names
    ) {
        checkArgument(types.size() == values.size());
        checkArgument(names == null || types.size() == names.size());
        this.types = types;
        this.values = values;
        this.names = new ArrayList(types.size());
        this.nameMap = new HashMap(types.size());
        for (int i=0; i<types.size(); i++) {
            final String name = names == null 
                    ? "_" + i
                    : names.get(i) == null ? "_" + i : names.get(i);
            this.names.add(i, name);
            this.nameMap.put(name, i);
        }
    }
    
    @Override
    public int size() {
        return types.size();
    }
    
    @Override
    public int index(@Nonnull String name) {
        Integer index = nameMap.get(checkNotNull(name));
        if (index != null) {
            return index;
        } else {
            throw new NoSuchElementException();
        }
    }
    
    @Override
    public String name(int i) {
        checkArgument(i>=0 && i<names.size());
        return names.get(i);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T value(int i) {
        checkArgument(i >=0 && i < values.size());
        return (T) values.get(i);
    }

    @Override
    public Class<?> type(int i) {
        checkArgument(i >=0 && i < values.size());
        return types.get(i);
    }

    @Override
    public Class<?> type(String typeName) {
        return type(index(typeName));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.types);
        hash = 23 * hash + Objects.hashCode(this.values);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TupleImpl other = (TupleImpl) obj;
        if (!Objects.equals(this.types, other.types)) {
            return false;
        }
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }

    public static final class Builder extends Tuple.Builder {
        
        private List<Class<?>> types;
        private List<Object> values;
        private List<String> names;
        
        Builder() {}
        
        @Override   // required & guaranteed to be called first
        public Tuple.Builder setTypes(@Nonnull final List<Class<?>> types) {
            checkNotNull(types);
            if (types.stream().anyMatch(t -> t == null)) {
                throw new RuntimeException("Nulls are not allowed.");
            }
            this.types = types;
            return this;
        }

        @Override   // required
        public Tuple.Builder setValues(@Nonnull final Object... values) {
            checkArgument(checkNotNull(values).length == types.size());
            this.values = Arrays.asList(values);
            return this;
        }
        
        @Override   // optional
        public Tuple.Builder setNames(@Nonnull final String... names) {
            checkArgument(checkNotNull(names).length == types.size());
            this.names = Arrays.asList(names);
            return this;
        }

        @Override
        public Tuple build() {
            if (types.isEmpty()) values = Collections.emptyList();
            checkNotNull(values);
            return new TupleImpl(types, values, names);
        }
    }
}
