/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmworks.util.ds;

import kmworks.util.ds.impl.PeekingArrayListStack;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cpl
 */
public class PeekingArrayListStackTest {
    
    public PeekingArrayListStackTest() {
    }

    @Test
    public void testIterator() {
        PeekingStack<Integer> stack = PeekingStack.of();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertTrue(3 == stack.top());
        assertTrue(2 == stack.peek(1));
        assertTrue(1 == stack.peek(2));
        Iterator<Integer> it = stack.iterator();
        assertTrue(it.hasNext());
        assertTrue(3 == it.next());
        assertTrue(it.hasNext());
        assertTrue(2 == it.next());
        assertTrue(it.hasNext());
        assertTrue(1 == it.next());
        assertFalse(it.hasNext());
    }
    
    @Test(expected = java.util.EmptyStackException.class)
    public void testThrowEmptyStackException() {
        PeekingStack<Integer> stack = PeekingStack.of();
        stack.top();
    }
    

}
