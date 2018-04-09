package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;
import kmworks.util.ds.rng.IntRangeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class SegmentedIntRangeTest {

    /*
        Invalid constructors
     */

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2a() {
        IntRange rng = IntRangeFactory.createIntRangePiecewise((IntRange) null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2b() {
        IntRange rng = IntRangeFactory.createIntRangePiecewise(IntRangeFactory.createIntRange(1, 2), (IntRange) null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2c() {
        IntRange rng = IntRangeFactory.createIntRangePiecewise((IntRange) null, IntRangeFactory.createIntRange(1, 2));
    }

    /*
        Construct from on-overlaping IntRange pieces
     */

    @Test
    public void testFromPieces_01() {
        IntRange rng = IntRangeFactory.createIntRangePiecewise(
                IntRangeFactory.createIntRange(5, 6),
                IntRangeFactory.createIntRange(4, 4),
                IntRangeFactory.createIntRange(1, 2)
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
}
