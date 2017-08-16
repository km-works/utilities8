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
package kmworks.util.base;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christian P. Lerch
 */
public class OptionTest {
  
  public OptionTest() {
  }

  @Test
  public void test1() {
    int value = 1;
    Option<Integer> opt1 = Option.of(value);
    assertTrue(opt1.isDefined());
    int result = opt1.get();
    assertEquals(value, result);
    Integer two = 2;
    assertNotEquals(two, opt1.getOrElse(2));
  }
  
  @Test
  public void test2() {
    Option<Integer> opt2 = Option.of();
    assertTrue(opt2.isEmpty());
    assertNull(opt2.orNull());
    Integer two = 2;
    assertEquals(two, opt2.getOrElse(2));
  }
  
  @Test
  public void testEqualsNone() {
    Option<String>  opt1 = Option.of();
    Option<Integer> opt2 = Option.of();
    assertEquals(opt1, opt2);
  }
  
}
