package kmworks.util.ds.rng.impl;

import com.google.common.collect.ImmutableSet;
import kmworks.util.ds.rng.IntRange;
import kmworks.util.ds.rng.IntRangeFactory;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class SegmentedIntRangeTest {

    /*
        Invalid constructors
     */

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2a() {
        IntRange rng = IntRangeFactory.createSegmentedIntRange((IntRange) null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2b() {
        IntRange rng = IntRangeFactory.createSegmentedIntRange(IntRangeFactory.createCompactIntRange(1, 2), (IntRange) null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2c() {
        IntRange rng = IntRangeFactory.createSegmentedIntRange((IntRange) null, IntRangeFactory.createCompactIntRange(1, 2));
    }

    /*
        Construct from on-overlaping IntRange pieces
     */

    @Test
    public void testFromPieces_01() {
        IntRange rng = IntRangeFactory.createSegmentedIntRange(
                IntRangeFactory.createCompactIntRange(5, 6),
                IntRangeFactory.createCompactIntRange(4, 4),
                IntRangeFactory.createCompactIntRange(1, 2)
        );
        assertTrue(rng.first() == 1);
        assertTrue(rng.last() == 6);
        assertTrue(rng.size() == 5);
        assertTrue(rng.contains(5));
        assertTrue(rng.contains(4));
        assertTrue(rng.contains(2));
        assertFalse(rng.contains(3));
        assertFalse(rng.contains(7));
    }

    @Test
    public void testFromSet_01() {
        IntRange rng = IntRangeFactory.createSegmentedIntRange(new ImmutableSet.Builder<Integer>()
                .add(1).add(2)
                .add(4)
                .add(5).add(6).build());
        assertTrue(rng.first() == 1);
        assertTrue(rng.last() == 6);
        assertTrue(rng.size() == 5);
        assertTrue(rng.contains(5));
        assertTrue(rng.contains(4));
        assertTrue(rng.contains(2));
        assertFalse(rng.contains(3));
        assertFalse(rng.contains(7));
    }

}
