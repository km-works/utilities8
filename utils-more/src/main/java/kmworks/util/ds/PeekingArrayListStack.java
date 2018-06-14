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
package kmworks.util.ds;

import java.util.ArrayList;



/**
 * A growable, typed stack implemented as an ArrayList.
 * * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>
 * @version 1.0.0
 * @since 1.0
 * @param <T> Type of stack item
 */
public final class PeekingArrayListStack<T> implements PeekingStack<T>{

  private static final int CAPACITY_INCR = 32;
  private final ArrayList<T> stack;
  private int itemCount = 0;  // empty stack
  private int capacity;

  /**
   * Create a growable stack for item type <T>
   */
  public PeekingArrayListStack() {
    this(CAPACITY_INCR);
  }

  /**
   * Create a growable stack for item type <T> withBounds given initial <size>
   * @param size
   */
  public PeekingArrayListStack(int size) {
    size = size > CAPACITY_INCR ? size : CAPACITY_INCR;
    stack = new ArrayList<>(size);   // create stack space of requested size
    capacity = size;
  }

  /**
   *  Push one <item> onto stack and increment item count
   *  @param item The item to be stacked
   */
  @Override
  public void push(final T item) {
    if (itemCount == capacity) {
      capacity += CAPACITY_INCR;
      stack.ensureCapacity(capacity);
    }
    stack.add(null);
    stack.set(itemCount, item);
    itemCount++;
  }

  /**
   * Remove one <item> from top of stack and decrement item count
   * @return Return item from top of stack
   */
  @Override
  public T pop() {
    if (isEmpty()) {
      throw new EmptyStackException("Attempt to pop from empty stack");
    }
    itemCount--;
    final T result = stack.get(itemCount);
    stack.set(itemCount, null);
    return result;
  }

  /**
   * Read top of stack without changing the stack
   * @return Return top of stack item
   */
  @Override
  public T top() {
    if (isEmpty()) {
      throw new EmptyStackException("Attempt to read from empty stack");
    }
    return peek(0);
  }

  /**
   * Read any stack element without changing the stack
   * @param pos
   * @return Return stack item at <pos> below top of stack
   */
  @Override
  public T peek(final int pos) {
    if (itemCount-pos-1 < 0) {
      throw new EmptyStackException("Attempt to peek past bottom of stack");
    }
    return stack.get(itemCount-pos-1);
  }

  /**
   * Clear all items from stack
   */
  @Override
  public void clear() {
    while (itemCount > 0) {
      stack.set(itemCount-1, null);
      itemCount--;
    }
  }

  /**
   * Get number of currently stacked items
   * @return Return stack size
   */
  @Override
  public int getSize() {
    return itemCount;
  }

  /**
   * Test if stack is empty
   * @return Return <true> if stack is empty
   */
  @Override
  public boolean isEmpty() {
    return itemCount == 0;
  }
}
class EmptyStackException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  public EmptyStackException() {
    this("Stack is empty");
  }
  public EmptyStackException(final String exception) {
    super(exception);
  }
}
