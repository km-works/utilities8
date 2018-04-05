/*
 * Copyright (C) 2005-2016 Christian P. Lerch <christian.p.lerch[at]gmail.com>
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kmworks.util.ds.rng;

import kmworks.util.ds.rng.*;
import kmworks.util.ds.rng.impl.CodepointBitSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christian P. Lerch
 */
public class CodepointPredicateTest {

    public CodepointPredicateTest() {
    }

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
    public void testAnd() {
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

    @Test
    public void testWithout() {
        CodepointPredicate digits = CodepointBitSet.fromRange('0', '9');
        CodepointPredicate six = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return cp == '6';
            }
        };

        CodepointPredicate setWithoutSix = digits.without(six);
        assertFalse(setWithoutSix.contains('6'));
        assertTrue(digits.without(six).contains('5'));
    }

    @Test
    public void realWorld01() {
        CodepointPredicate cp1 = CodepointBitSet.of(CodepointSetUtil.codepointsFrom("\"\\\f\u201c\u201d\u2028\u2029"));
        CodepointPredicate cp2 = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.getType(cp) == Character.CONTROL;
            }
        };
        CodepointPredicate invalidStringCharSet = CodepointSetUtil.or(cp1, cp2);
        assertTrue(invalidStringCharSet.contains(0));
        assertTrue(invalidStringCharSet.contains('\f'));
        assertTrue(invalidStringCharSet.contains('\\'));
        assertTrue(invalidStringCharSet.contains('\"'));
        assertFalse(invalidStringCharSet.contains(' '));
    }

}
