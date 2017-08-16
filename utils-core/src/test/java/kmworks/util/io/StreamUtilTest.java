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
package kmworks.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christian P. Lerch
 */
public class StreamUtilTest {
  
  public StreamUtilTest() {
  }

  @Test
  public void testDeepCopy1() throws Exception {
        // Make a reasonable large test object. Note that this doesn't
        // do anything useful -- it is simply intended to be large, have
        // several levels of references, and be somewhat random. We start
        // with a hashtable and add vectors to it, where each element in
        // the vector is a Date object (initialized to the current time),
        // a semi-random string, and a (circular) reference back to the
        // object itself. In this case the resulting object produces
        // a serialized representation that is approximate 700K.
        final Map<Integer, Object[]> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(i, new Object[] { 
                    new Date(), 
                    "A random number: " + Math.random(),
                    i
                 });
        } 

        final int iterations = 50;

        // Make copies of the object using the unoptimized version
        // of the deep copy utility.
        long unoptimizedTime = 0L;
        for (int i = 0; i < iterations; i++) {
            final long start = System.nanoTime();
            Object copy = unoptimizedDeepCopy((Object)map);
            unoptimizedTime += (System.nanoTime() - start);

            // Avoid having GC run while we are timing...
            copy = null;
            System.gc();
        }

        // Repeat with the optimized version
        long optimizedTime = 0L;
        for (int i = 0; i < iterations; i++) {
            final long start = System.nanoTime();
            Object copy = StreamUtil.deepCopy((Object)map);
            optimizedTime += (System.nanoTime() - start);

            // Avoid having GC run while we are timing...
            copy = null;
            System.gc();
        }

        System.out.println("Unoptimized time: " + (unoptimizedTime/iterations)/1000 + " µsec");
        System.out.println("  Optimized time: " + (optimizedTime/iterations)/1000 + " µsec");
        System.out.println("     Buffer Size: " + StreamUtil.bufferUsed);
        System.out.println("      Speed gain: " + (new Double(unoptimizedTime)/optimizedTime));
  }

   /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
    private static Object unoptimizedDeepCopy(Object orig) throws Exception {
        Object obj = null;
        // Write the object out to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(orig);
        out.flush();
        out.close();
        // Make an input stream from the byte array and read
        // a copy of the object back in.
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        obj = in.readObject();
        return obj;
    }
}
