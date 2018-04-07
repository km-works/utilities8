package kmworks.util.ds.rng.impl;

import kmworks.util.ds.rng.IntRange;
import kmworks.util.ds.rng.IntRangeFactory;
import org.junit.Test;

public class IntRangeSeqTest {

    /*
        Invalid constructors
     */

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor_1() {
        IntRange rng = new IntRangeSeq();
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2a() {
        IntRange rng = new IntRangeSeq((IntRange) null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2b() {
        IntRange rng = new IntRangeSeq(IntRangeFactory.createIntRange(1, 2), (IntRange) null);
    }

    @Test(expected = NullPointerException.class)
    public void testInvalidConstructor_2c() {
        IntRange rng = new IntRangeSeq((IntRange) null, IntRangeFactory.createIntRange(1, 2));
    }

}
