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

import kmworks.util.ds.impl.PeekingArrayListStack;

/**
 *
 * * @author Christian P. Lerch (christian.p.lerch[at]gmail.com)
 * @param <T>
 */
public interface PeekingStack<T> extends Iterable<T> {
    
  static <T> PeekingStack<T> of() { return new PeekingArrayListStack<>(); }
  static <T> PeekingStack<T> of(final int size) { return new PeekingArrayListStack<>(size); }

  /**
   *  Push one item onto stack and increment item count
   *  @param item The item to be stacked
   */
  public void push(final T item);

  /**
   * Remove one item from top of stack and decrement item count
   * @return Return item from top of stack
   */
  public T pop();

  /**
   * Read top of stack without changing the stack
   * @return Return top of stack item
   */
  public T top();

  /**
   * Read any stack element without changing the stack
   * @param pos
   * @return Return stack item at 0-based pos below top of stack
   */
  public T peek(final int pos);

  /**
   * Clear stack by poping all its items
   */
  public void clear();

  /**
   * Get number of currently stacked items
   * @return Return stack size
   */
  public int getSize();

  /**
   * Test if stack is empty
   * @return Return true if stack is empty
   */
  public boolean isEmpty();
  
}
