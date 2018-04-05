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

import java.io.StringWriter;

import kmworks.util.ds.rng.CodepointSet;
import kmworks.util.ds.rng.CodepointSetUtil;
import kmworks.util.ds.rng.impl.CodepointBitSet;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Christian P. Lerch <christian.p.lerch[at]gmail.com>
 */
public class CodepointSetUtilTest {
  
  public CodepointSetUtilTest() {
  }

  @Test
  public void testCodepointsFrom() {
  }

  @Test
  public void testFromText() throws Exception {
  }

  @Test
  public void testToText() throws Exception {
    StringWriter w = new StringWriter();
    CodepointSet cps = CodepointBitSet.of(5,6,8,10,11,15);
    CodepointSetUtil.toText(cps, w, 10);
    String result = w.toString();
    System.out.println(result);
    assertEquals("5-6\n8\n10-11\n15\n", result);
  }

  @Test @Ignore
  public void testA() {
    int cnt = 0;
    int max = 0, min = 0;
    for (int i=0; i<=0xffff; i++) {
      if (Character.isJavaIdentifierStart(i)) {
        cnt += 1;
        if (i > max) max = i;
        if (i < min) min = i;
      }
    }
    System.out.println(String.format("%d JavaIdentifierStart characters; range: %d..%d (span=%d)",
            cnt, min, max, max-min+1));
  }

}
