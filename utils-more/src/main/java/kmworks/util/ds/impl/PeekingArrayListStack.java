/*
 *   Copyright (C) 2012 Christian P. Lerch, Vienna, Austria.
 *
 *   This file is part of kmworks-util - a versatile Java utilites library.
 *
 *   kmworks-util is free software: you can use, modify and redistribute it under
 *   the terms of the GNU General Public License version 3 as published by
 *   the Free Software Foundation, Inc. <http://fsf.org/>
 *
 *   kmworks-util is distributed in the hope that it will be useful, but WITHOUT
 *   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *   version 3 for details.
 *
 *   You should have received a copy of the GNU General Public License along
 *   withBounds this distribution. If not, see <http://km-works.eu/res/GPL.txt> or
 *   <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package kmworks.util.ds.impl;

import com.google.common.collect.AbstractIterator;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import kmworks.util.ds.PeekingStack;

/**
 * An expanding, typed, peekable and iterable stack implemented as an ArrayList.
 *
 * @author Christian P. Lerch (christian.p.lerch[at]gmail.com)
 * @version 1.0.0
 * @since 1.0
 * @param <T> Type of stack item
 */
public final class PeekingArrayListStack<T> implements PeekingStack<T> {

    private static final int CAPACITY_INCR = 10;
    private final ArrayList<T> backingArrayList;
    private int itemCount = 0;  // empty stack
    private int capacity;

    /**
     * Create a growable stack for item type <code>T</code>
     */
    public PeekingArrayListStack() {
        this(CAPACITY_INCR);
    }

    /**
     * Create a growable stack for item type <code>T</code> withBounds given initial <code>size</code>
     *
     * @param size
     */
    public PeekingArrayListStack(final int size) {
        capacity = size > CAPACITY_INCR ? size : CAPACITY_INCR;
        backingArrayList = new ArrayList<>(capacity);   // create stack space of requested size, at least CAPACITY_INCR
    }

    /**
     * Push one item onto stack and increment item count
     *
     * @param item The item to be stacked
     */
    @Override
    public void push(final T item) {
        if (itemCount == capacity) {
            capacity += CAPACITY_INCR;
            backingArrayList.ensureCapacity(capacity);
        }
        backingArrayList.add(null);
        backingArrayList.set(itemCount, item);
        itemCount++;
    }

    /**
     * Remove one item from top of stack and decrement item count
     *
     * @return Return item from top of stack
     */
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        itemCount--;
        final T result = backingArrayList.get(itemCount);
        backingArrayList.set(itemCount, null);
        return result;
    }

    /**
     * Read top of stack without changing the stack
     *
     * @return Return top of stack item
     */
    @Override
    public T top() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return peek(0);
    }

    /**
     * Return any stack element without changing the stack.
     *
     * @param pos position of stack item to return; pos => 0 returns top of stack.
     * @return Return stack item at <pos> below top of stack
     */
    @Override
    public T peek(final int pos) {
        if (itemCount - pos - 1 < 0) {
            throw new EmptyStackException();
        }
        return backingArrayList.get(itemCount - pos - 1);
    }

    /**
     * Clear all items from stack
     */
    @Override
    public void clear() {
        while (itemCount > 0) {
            backingArrayList.set(itemCount - 1, null);
            itemCount--;
        }
    }

    /**
     * Get number of currently stacked items
     *
     * @return Return stack size
     */
    @Override
    public int getSize() {
        return itemCount;
    }

    /**
     * Test if stack is empty
     *
     * @return Return <code>true</code> if stack is empty
     */
    @Override
    public boolean isEmpty() {
        return itemCount == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new AbstractIterator<T>() {
            int currPos = 0;
            @Override
            protected T computeNext() {
                if (currPos == itemCount) {
                    return endOfData();
                } else {
                    return PeekingArrayListStack.this.peek(currPos++);
                }
            }
        };
    }
}
