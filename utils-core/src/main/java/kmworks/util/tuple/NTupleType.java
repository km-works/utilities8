/*
 * Copyright (C) 2005-2016 Christian P. Lerch <christian.p.lerch[at]gmail.com>
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.tuple;

import java.io.Serializable;

/**
 * Represents the type of an n-tuple. 
 * Used to define a type of n-tuple and then create tuples of that type.
 *
 * http://stackoverflow.com/questions/3642452/java-n-tuple-implementation/3642623#3642623
 */
public interface NTupleType extends Serializable {

    int size();

    Class<?> type(int i);

    /**
     * Tuple are immutable objects.  Tuples should contain only immutable objects or
     * objects that won't be modified while part of a tuple.
     *
     * @param values
     * @return Tuple with the given values
     * @throws IllegalArgumentException if the wrong # of arguments or incompatible tuple values are provided
     */
    NTuple createTuple(Object... values);

    class DefaultFactory {
        public static NTupleType create(final Class<?>... types) {
            return new NTupleTypeImpl(types);
        }
    }

}