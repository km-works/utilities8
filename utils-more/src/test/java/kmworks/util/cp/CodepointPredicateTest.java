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
package kmworks.util.cp;

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
        CodepointPredicate digit = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isDigit(cp);
            }
        };
        assertTrue(digit.contains((int) '9'));
    }

    @Test
    public void testAnd() {
    }

    @Test
    public void testOr_1() {
        CodepointPredicate digit = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isDigit(cp);
            }
        };
        CodepointPredicate letter = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isLetter(cp);
            }
        };
        CodepointPredicate letterOrDigit = letter.or(digit);
        assertTrue(letterOrDigit.contains((int) '6'));
        assertTrue(letterOrDigit.contains((int) 'Ϣ'));
    }

    @Test
    public void testNot() {
        CodepointPredicate digit = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return Character.isDigit(cp);
            }
        };
        CodepointPredicate noDigit = digit.negate();
        assertFalse(noDigit.contains((int) '6'));
        assertTrue(noDigit.contains((int) '©'));
    }

    @Test
    public void testWithout() {
        CodepointPredicate digits = CodepointBitSet.fromRange('0', '9');
        CodepointPredicate six = new CodepointPredicate() {
            @Override
            public boolean contains(int cp) {
                return cp == (int) '6';
            }
        };
        // down-cast would throw ClassCastException:
        // CodepointSet setWithoutSix = ((CodepointSet) digits.without(six));
        // CodepointBitSet bitsetWithoutSix = ((CodepointBitSet) digits.without(six));
        assertFalse(digits.without(six).contains((int) '6'));
        assertTrue(digits.without(six).contains((int) '5'));
    }

    @Test
    public void realWorld01() {
        CodepointPredicate invalidStringCharSet
                = ((CodepointPredicate) CodepointBitSet.of(
                        CodepointSetUtil.codepointsFrom("\"\\\f\u201c\u201d\u2028\u2029")))
                        .or(new CodepointPredicate() {
                            @Override
                            public boolean contains(int cp) {
                                return Character.getType(cp) == Character.CONTROL;
                            }
                        });
        assertTrue(invalidStringCharSet.contains(0));
        assertTrue(invalidStringCharSet.contains('\f'));
        assertTrue(invalidStringCharSet.contains('\\'));
        assertFalse(invalidStringCharSet.contains(' '));
    }

}
