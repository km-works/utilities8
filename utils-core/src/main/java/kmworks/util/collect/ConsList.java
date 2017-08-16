/*BEGIN_COPYRIGHT_BLOCK*

PLT Utilities BSD License

Copyright (c) 2007-2010 JavaPLT group at Rice University
All rights reserved.

Developed by:   Java Programming Languages Team
                Rice University
                http://www.cs.rice.edu/~javaplt/

Redistribution and use in source and binary forms, with or without modification, are permitted 
provided that the following conditions are met:

    - Redistributions of source code must retain the above copyright notice, this list of conditions 
      and the following disclaimer.
    - Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials provided 
      with the distribution.
    - Neither the name of the JavaPLT group, Rice University, nor the names of the library's 
      contributors may be used to endorse or promote products derived from this software without 
      specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*END_COPYRIGHT_BLOCK*/
package kmworks.util.collect;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kmworks.util.lambda.Function1;
import kmworks.util.lambda.Predicate1;
import kmworks.util.iter.EmptyIterator;
import kmworks.util.iter.SizedIterable;
import kmworks.util.iter.UnmodifiableIterator;

/**
 * <p>
 * A Lisp- or Scheme-style immutable list. A {@code ConsList} is either an {@link Empty} or a {@link Nonempty};
 * operations may be defined on the lists by implementing {@link ConsVisitor}. While
 * {@link edu.rice.cs.plt.iter.ComposedIterable} provides similar facilities for creating lists, {@code ConsList}s
 * provide a more efficient means of <em>decomposing</em> lists -- because there is no {@code rest} operation on
 * {@code Iterable}s in general, the only way to produce an {@code Iterable} sublist of an {@code Iterable} is to make a
 * copy. With a well-designed {@code ConsList}, on the other hand, producing a sublist is trivial.</p>
 *
 * <p>
 * This class also defines a few static convenience methods to simplify client code. A static import
 * ({@code import static edu.rice.cs.plt.collect.ConsList.*}) will eliminate the need for explicit name qualifiers when
 * using these methods.</p>
 *
 * @param <T>
 */
public abstract class ConsList<T> implements SizedIterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    public abstract <Ret> Ret apply(ConsVisitor<? super T, ? extends Ret> visitor);

    @Override
    public abstract Iterator<T> iterator();

    /**
     * Whether this is an empty ConsList.
     * @return 
     */
    @Override
    public abstract boolean isEmpty();

    /**
     * Compute the size of the list. Note that this is a linear &mdash; not constant-time &mdash; operation.
     * @return 
     */
    @Override
    public abstract int size();

    /**
     * Compute the size of the list, up to a given bound. Note that this is a linear &mdash; not constant-time &mdash;
     * operation.
     * @return 
     */
    @Override
    public abstract int size(int bound);

    /**
     * Return {@code false}: cons lists are not infinite.
     * @return 
     */
    @Override
    public boolean isInfinite() {
        return false;
    }

    /**
     * Return {@code true}: cons lists have a fixed size.
     * @return 
     */
    @Override
    public boolean hasFixedSize() {
        return true;
    }

    /**
     * Return {@code true}: cons lists are immutable.
     * @return 
     */
    @Override
    public boolean isImmutable() {
        return true;
    }

    public int compositeHeight() {
        return size();
    }

    public int compositeSize() {
        return size() + 1;
        /* add 1 for empty */ }

    /**
     * * Create an empty list (via {@link Empty#of})
     */
    public static <T> Empty<T> empty() {
        return Empty.<T>of();
    }

    /**
     * Create a nonempty list (via {@link ConsList#ConsList()})
     */
    public static <T> Nonempty<T> cons(T first, ConsList<? extends T> rest) {
        return new Nonempty<>(first, rest);
    }

    /**
     * Create a singleton nonempty list (via {@link ConsList#ConsList()})
     */
    public static <T> Nonempty<T> singleton(T value) {
        return new Nonempty<>(value, Empty.<T>of());
    }

    /**
     * Attempt to access the first of the given list (throws an exception in the empty case).
     */
    public static <T> T first(ConsList<? extends T> list) {
        return list.apply(ConsVisitor.<T>first());
    }

    /**
     * Attempt to access the rest of the given list (throws an exception in the empty case).
     */
    public static <T> ConsList<? extends T> rest(ConsList<? extends T> list) {
        return list.apply(ConsVisitor.<T>rest());
    }

    /**
     * Reverse the given list
     */
    public static <T> ConsList<? extends T> reverse(ConsList<? extends T> list) {
        return list.apply(ConsVisitor.<T>reverse());
    }

    /**
     * Filter the given list according to a predicate
     */
    public static <T> ConsList<? extends T> filter(ConsList<? extends T> list, 
            Predicate1<? super T> pred) {
        return list.apply(ConsVisitor.<T>filter(pred));
    }

    /**
     * Map the given list according to a lambda
     */
    public static <S, T> ConsList<? extends T> map(ConsList<? extends S> list, 
            Function1<? super S, ? extends T> lambda) {
        return list.apply(ConsVisitor.map(lambda));
    }

    /**
     * The empty variant of a {@code ConsList}. Instances are made available via {@link #of}.
     */
    public static class Empty<T> extends ConsList<T> {

        private static final long serialVersionUID = 1L;

        /**
         * * Force use of {@link #of}
         */
        private Empty() {
        }

        private static final Empty<Void> INSTANCE = new Empty<>();

        /**
         * Creates an empty list. The result is a singleton, cast (unsafe formally, but safe in practice) to the
         * appropriate type
         */
        @SuppressWarnings("unchecked")
        public static <T> Empty<T> of() {
            return (Empty<T>) INSTANCE;
        }

        /**
         * Invoke the {@code forEmpty} case of a visitor
         */
        @Override
        public <Ret> Ret apply(ConsVisitor<? super T, ? extends Ret> visitor) {
            return visitor.forEmpty();
        }

        /**
         * Return an empty iterator
         */
        @Override
        public Iterator<T> iterator() {
            return EmptyIterator.of();
        }

        /**
         * Return {@code true}.
         */
        @Override
        public boolean isEmpty() {
            return true;
        }

        /**
         * Return {@code 0}
         */
        @Override
        public int size() {
            return 0;
        }

        /**
         * Return {@code 0}
         */
        @Override
        public int size(int bound) {
            return 0;
        }

    }

    /**
     * The nonempty variant of a {@code ConsList}. Contains a <em>first</em> value of the element type and a
     * <em>rest</em> which is another list.
     */
    public static class Nonempty<T> extends ConsList<T> {

        private static final long serialVersionUID = 1L;

        private final T _first;
        private final ConsList<? extends T> _rest;

        public Nonempty(T first, ConsList<? extends T> rest) {
            _first = first;
            _rest = rest;
        }

        public T first() {
            return _first;
        }

        public ConsList<? extends T> rest() {
            return _rest;
        }

        /**
         * Invoke the {@code forNonempty} case of a visitor
         */
        public <Ret> Ret apply(ConsVisitor<? super T, ? extends Ret> visitor) {
            return visitor.forNonempty(_first, _rest);
        }

        /**
         * Create an iterator to traverse the list
         */
        public Iterator<T> iterator() {
            return new UnmodifiableIterator<T>() {
                private ConsList<? extends T> _current = Nonempty.this;

                public boolean hasNext() {
                    return !_current.isEmpty();
                }

                public T next() {
                    return _current.apply(new ConsVisitor<T, T>() {
                        public T forEmpty() {
                            throw new NoSuchElementException();
                        }

                        public T forNonempty(T first, ConsList<? extends T> rest) {
                            _current = rest;
                            return first;
                        }
                    });
                }
            };
        }

        /**
         * Return {@code false}.
         */
        public boolean isEmpty() {
            return false;
        }

        /**
         * Return {@code 1 + rest.size()}.
         */
        public int size() {
            return 1 + _rest.size();
        }

        /**
         * Return {@code 1 + rest.size(bound - 1)}, or {@code 0} if {@code bound == 0}.
         */
        public int size(int bound) {
            if (bound == 0) {
                return 0;
            } else {
                return 1 + _rest.size(bound - 1);
            }
        }

    }

}
