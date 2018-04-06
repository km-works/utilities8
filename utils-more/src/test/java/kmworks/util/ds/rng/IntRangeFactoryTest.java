package kmworks.util.ds.rng;

import com.google.common.collect.ImmutableSet;
import junit.framework.Assert;
import kmworks.util.ds.rng.impl.ContiguousIntRange;
import kmworks.util.ds.rng.impl.DiscontiguousIntRange;
import org.junit.Test;

import java.util.Set;

import static junit.framework.TestCase.*;

public class IntRangeFactoryTest {

    @Test
    public void testCreateContiguousIntRange() {
        IntRange rng = IntRangeFactory.createIntRange(1, 10);
        assertTrue(rng instanceof ContiguousIntRange);
        assertEquals(10, rng.span());
        assertEquals(rng.size(), rng.span());
        assertTrue(rng.contains(1));
        assertTrue(rng.contains(3));
        assertTrue(rng.contains(7));
        assertTrue(rng.contains(10));
        assertFalse(rng.contains(0));
        assertFalse(rng.contains(11));
    }

    @Test
    public void testCreateDiscontiguousIntRange() {
        Set<Integer> set = new ImmutableSet.Builder<Integer>().add(1,3,7,10).build();
        IntRange rng = IntRangeFactory.createIntRange(set);
        assertTrue(rng instanceof DiscontiguousIntRange);
        assertEquals(10, rng.span());
        assertNotSame(rng.size(), rng.span());
        assertTrue(rng.contains(1));
        assertTrue(rng.contains(3));
        assertTrue(rng.contains(7));
        assertTrue(rng.contains(10));
        assertFalse(rng.contains(0));
        assertFalse(rng.contains(2));
        assertFalse(rng.contains(5));
        assertFalse(rng.contains(11));
    }
}
