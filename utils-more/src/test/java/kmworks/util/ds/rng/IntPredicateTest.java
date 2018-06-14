package kmworks.util.ds.rng;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntPredicateTest {

    @Test
    public void testInstantiation_1() {
        IntPredicate digit = new IntPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isDigit(cp);
            }
        };
        assertTrue(digit.contains('9'));
    }

    @Test
    public void testOr_1() {
        IntPredicate digit = new IntPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isDigit(cp);
            }
        };
        IntPredicate letter = new IntPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isLetter(cp);
            }
        };
        IntPredicate letterOrDigit = letter.or(digit);
        assertTrue(letterOrDigit.contains('6'));
        assertTrue(letterOrDigit.contains('Ϣ'));
    }

    @Test
    public void testNot() {
        IntPredicate digit = new IntPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isDigit(cp);
            }
        };
        IntPredicate noDigit = digit.negate();
        assertFalse(noDigit.contains('6'));
        assertTrue(noDigit.contains('©'));
    }

    /*
    @Test
    public void testWithout() {
        CodepointPredicate digits = BitsetCodepointRange.fromRange('0', '9');
        CodepointPredicate six = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return cp == '6';
            }
        };

        CodepointPredicate setWithoutSix = digits.without(six);
        assertFalse(setWithoutSix.contains('6'));
        assertTrue(digits.without(six).contains('5'));
    }*/

    @Test
    public void realWorld01() {
        IntRange cp1 = IntRangeBuilder.withBounds(IntRange.UNICODE_BOUNDS)
                .addAll(IntRangeUtil.codepointsFrom("\"\\\f\u201c\u201d\u2028\u2029"))
                .build();
        IntPredicate cp2 = new IntPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.getType(cp) == Character.CONTROL;
            }
        };
        IntPredicate invalidStringCharSet = IntRangeUtil.or(cp1, cp2);
        assertTrue(invalidStringCharSet.contains(0));
        assertTrue(invalidStringCharSet.contains('\f'));
        assertTrue(invalidStringCharSet.contains('\\'));
        assertTrue(invalidStringCharSet.contains('\"'));
        assertFalse(invalidStringCharSet.contains(' '));
    }
}
