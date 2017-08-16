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
package kmworks.util.misc;

import com.google.common.primitives.Longs;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Christian P. Lerch
 */
public final class UUIDGenerator {
  
  private UUIDGenerator() {}
  
  public static UUID generate() {
    
    Calendar cal = Calendar.getInstance();
    int yy = cal.get(Calendar.YEAR) - 2000;     // <= 99  < 128
    int ddd = cal.get(Calendar.DAY_OF_YEAR);    // <= 366 < 512
    
    long msbLong = (yy << 9) | ddd;
    msbLong = msbLong << 48;
    byte hiBytes[] = new byte[6];
    ThreadLocalRandom.current().nextBytes(hiBytes);
    byte loBytes[] = new byte[8];
    ThreadLocalRandom.current().nextBytes(loBytes);
    msbLong |= from6Bytes(hiBytes);
    
    long lsbLong = Longs.fromByteArray(loBytes);
    UUID uuid = new UUID(msbLong, lsbLong);
    
    return uuid;
  }
  
  private static long from6Bytes(byte[] bytes) {
    return (bytes[0] & 0xFFL) << 40
         | (bytes[1] & 0xFFL) << 32
         | (bytes[2] & 0xFFL) << 24
         | (bytes[3] & 0xFFL) << 16
         | (bytes[4] & 0xFFL) << 8
         | (bytes[5] & 0xFFL);
  }
  
}
