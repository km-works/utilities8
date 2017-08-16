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

import kmworks.util.cp.CodepointBitSet;
import kmworks.util.cp.CodepointSetUtil;
import kmworks.util.cp.CodepointSet;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Christian P. Lerch
 */
public class CodepointBitSetTest {
  
  private CodepointBitSet.Builder randomBuilder(int setSize, int maxCodepoint) 
  {
    final Random RANDOM = new Random();
    final CodepointBitSet.Builder builder = new CodepointBitSet.Builder();
    for (int j=0; j < setSize; j++) {
      builder.add(RANDOM.nextInt(maxCodepoint));
    }
    return builder;
  }
  
  private void pln(String s) {
    System.out.println(s);
  }

  public CodepointBitSetTest() {
  }

  @Test
  public void test() throws Exception {
    CodepointSet bs = CodepointBitSet.of(44, 50);
    assertTrue(!bs.contains(43));
    assertTrue(bs.contains(44));
    assertTrue(bs.contains(50));
    assertTrue(!bs.contains(51));
    assertTrue(bs.size() == 2);
  }

  @Test
  public void testSingularity() throws Exception {
    CodepointSet bs = new CodepointBitSet.Builder()
            .addRange(33000, 33000)
            .add(33000)
            .build();
    assertTrue(!bs.contains(32999));
    assertTrue(bs.contains(33000));
    assertTrue(!bs.contains(34));
    assertEquals(1, bs.size());
    assertEquals(1, ((CodepointBitSet)bs).bucketsCount());
  }

  @Test
  public void testLoader() throws Exception {
    String initialBits = "  # uoöhef \n  30 -40  #p owr\n63\n\n128 #jnoö  wetä\n80-82";
    StringReader r = new StringReader(initialBits);
    CodepointSet bs = CodepointSetUtil.fromText(r, 10);
    assertTrue(bs.size() == 16);
    assertTrue(bs.contains(30));
    assertTrue(bs.contains(40));
    assertTrue(bs.contains(63));
    assertTrue(bs.contains(80));
    assertTrue(bs.contains(128));
    assertTrue(!bs.contains(29));
    assertTrue(!bs.contains(41));
  }


  /*
   * Equality
   */
  @Test
  public void testNonEquals1() throws Exception {
    CodepointSet bs1 = new CodepointBitSet.Builder()
            .addRange(33000, 33000)
            .build();
    CodepointSet bs2 = new CodepointBitSet.Builder()
            .addRange(32950, 33050)
            .build();
    assertEquals(bs1.size(), 1);
    assertEquals(bs2.size(), 101);
    assertTrue(bs1.contains(33000));
    assertTrue(bs2.contains(33000));
    assertFalse(bs1.equals(bs2));
  }
  
  @Test
  public void testEquals() throws Exception {
    CodepointSet bs1 = new CodepointBitSet.Builder().addRange(5, 10).add(20).build();
    CodepointSet bs2 = new CodepointBitSet.Builder().addRange(5, 10).add(20).build();
    assertEquals(bs1, bs2);
    assertEquals(bs1.hashCode(), bs2.hashCode());
  }
  
  @Test
  public void testSetEquivalence() 
  {
    final int maxCodepoint = 0xffff;
    final int iterations = 1000;
    
    for (int i=0; i<iterations; i++) 
    {
      final CodepointBitSet.Builder builder = randomBuilder(1000, maxCodepoint);
      final SortedSet<Integer> set = builder.get();
      final CodepointSet bs = builder.build();
      
      assertEquals(set.size(), bs.size());
      assertEquals(set.first(), bs.first());
      assertEquals(set.last(), bs.last());
      
      final Iterator<Integer> setIter = set.iterator();
      final Iterator<Integer> bsIter = bs.iterator();
      while (setIter.hasNext() && bsIter.hasNext()) {
        assertEquals(setIter.next(), bsIter.next());
      }
    }
  }
  
  @Test
  public void testEmptyEquals1() {
    CodepointSet bs1 = CodepointBitSet.of();
    CodepointSet bs2 = CodepointBitSet.of();
    assertTrue(bs1 == bs2);
    assertEquals(bs1, bs2);
  }

  @Test
  public void testEmptyEquals2() {
    CodepointSet bs1 = CodepointBitSet.of();
    CodepointSet bs2 = CodepointBitSet.of(1,2,3);
    CodepointSet bs3 = CodepointBitSet.of(4,5,6);
    CodepointSet bs4 = bs2.intersect(bs3);  // -> yields EMPTY
    assertTrue(bs1 == bs4);
    assertEquals(bs1, bs4);
  }

  /*
   * Intersect
   */
  @Test
  public void testIntersect1() {
    CodepointSet bs1 = CodepointBitSet.fromRange(10, 50);
    CodepointSet bs2 = CodepointBitSet.fromRange(20, 60);
    
    assertEquals(bs1.size(), 41);
    assertEquals(bs1.size(), bs2.size());
    
    bs1 = bs1.intersect(bs2);
    
    assertEquals(bs1.size(), 31);
    assertTrue(bs1.contains(20));
    assertTrue(bs1.contains(50));
    assertTrue(!bs1.contains(19));
    assertTrue(!bs1.contains(51));
  }

  /*
   * Union
   */
  @Test
  public void testUnion1() throws Exception {
    // bs1 << bs2, no overlap
    CodepointSet bs1 = CodepointBitSet.of(20, 40);
    CodepointSet bs2 = CodepointBitSet.of(50, 70);
    bs1 = bs1.union(bs2);

    assertTrue(bs1.contains(20));
    assertTrue(bs1.contains(40));
    assertTrue(bs1.contains(50));
    assertTrue(bs1.contains(70));
    assertTrue(bs1.size() == 4);
    assertTrue(bs1.first() == 20);
    assertTrue(bs1.last() == 70);
  }

  @Test
  public void testUnion2() throws Exception {
    // bs1 >> bs2, no overlap
    CodepointSet bs1 = CodepointBitSet.of(50, 70);
    CodepointSet bs2 = CodepointBitSet.of(20, 40);
    bs1 = bs1.union(bs2);

    assertTrue(bs1.contains(20));
    assertTrue(bs1.contains(40));
    assertTrue(bs1.contains(50));
    assertTrue(bs1.contains(70));
    assertTrue(bs1.size() == 4);
    assertTrue(bs1.first() == 20);
    assertTrue(bs1.last() == 70);
  }
  
  @Test
  public void testRandomEquals() {
    long start, ende;
    long equalsDuration = 0, hashDuration = 0, newDuration = 0, buildDuration = 0;
    final int ITERATIONS = 1000;
    final Random rand = new Random();
    
    for (int k=1; k<=10; k++) {
      int SET_SIZE = 100 * k;
      for (int i=0; i < ITERATIONS; i++) {
        final CodepointBitSet.Builder builder = new CodepointBitSet.Builder();
        start = System.nanoTime();
        for (int j=0; j < SET_SIZE; j++) {
          builder.add(rand.nextInt(CodepointSet.MAX_CODEPOINT));
        }
        ende = System.nanoTime();
        buildDuration += (ende - start);

        start = System.nanoTime();
        final CodepointSet bs1 = builder.build();
        final CodepointSet bs2 = builder.build();
        ende = System.nanoTime();
        newDuration += (ende - start);

        start = System.nanoTime();
        final boolean isEqual = bs1.equals(bs2);
        ende = System.nanoTime();
        equalsDuration += (ende - start);
        assertTrue(isEqual);

        start = System.nanoTime();
        final int hash1 = bs1.hashCode();
        final int hash2 = bs2.hashCode();
        ende = System.nanoTime();
        hashDuration += (ende - start);
        assertEquals(hash1, hash2);
      }
      System.out.println(String.format("SetSize: %d -- build: %d msec  / create: %d msec  /  equals: %d msec  /  hash: %d msec",
              SET_SIZE,
              buildDuration/1000/ITERATIONS, 
              newDuration/2000/ITERATIONS, 
              equalsDuration/1000/ITERATIONS, 
              hashDuration/2000/ITERATIONS));
    }
  }

  @Test
  public void testRandomAccessPerformance() {
    long start, ende;

    start = System.currentTimeMillis();
    final CodepointBitSet.Builder builder = new CodepointBitSet.Builder();
    final int setsize = 100000;
    final Random rand = new Random();
    for (int i = 0; i < setsize; i++) {
      builder.add(rand.nextInt(CodepointSet.MAX_CODEPOINT));
    }
    CodepointSet bs = builder.build();
    ende = System.currentTimeMillis();
    System.out.println(String.format("Timing1: build a %d codepoints random bitset in %d msec", bs.size(), (ende - start)));

    final int lookups = 10000000;
    start = System.currentTimeMillis();
    for (int i = 0; i < lookups; i++) {
      bs.contains(rand.nextInt(CodepointSet.MAX_CODEPOINT));
    }
    ende = System.currentTimeMillis();
    System.out.println(String.format("Timing2: Do %d random lookups in %d msec (%d lookups /sec)"
            , lookups, (ende-start), 1000 * (lookups / (ende - start))));
  }
  
  @Test
  public void testIterator_1() {
    List<Integer> values = Arrays.asList(new Integer[]{1,3,6,7,10,14,15,90});
    CodepointSet cps = CodepointBitSet.of(values);
    assertEquals(values.size(), cps.size());
    Iterator<Integer> listIter = values.iterator();
    Iterator<Integer> cpsIter = cps.iterator();
    while (cpsIter.hasNext()) {
      Integer cp = cpsIter.next();
      Integer val = listIter.next();
      assertEquals(cp, val);
      pln(cp.toString());
    }
  }

}
