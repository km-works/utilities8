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

import java.net.URI;
import org.junit.Test;
import static org.junit.Assert.*;
import static kmworks.util.base.Try.*;

/**
 *
 * @author Christian P. Lerch
 */
public class TryTest {
  
  public TryTest() {
  }

  @Test
  public void testTryFailure() {
    Try<URI> tryUri = try_(() -> new URI("<<<>>>"));
    
    if(tryUri.isSuccess()) {
      assertTrue(tryUri instanceof Success);
      URI uri = tryUri.get();
      System.out.println("Success["+uri.toString()+"]");
    } else {
      assertTrue(tryUri instanceof Failure);
      try { tryUri.get(); }
      catch (WrappedException we) { 
        Throwable cause = we.getCause();
        System.out.println(String.format("Failure[%s: %s]", 
                cause.getClass().getName(), cause.getMessage()));
      }
    }
  }
  
  @Test
  public void testTrySuccess() {
    Try<URI> tryUri = try_(() -> new URI("http://localhost/"));
    
    if(tryUri.isSuccess()) {
      assertTrue(tryUri instanceof Success);
      URI uri = tryUri.get();
      System.out.println("Success["+uri.toString()+"]");
    } else {
      assertTrue(tryUri instanceof Failure);
      try { tryUri.get(); }
      catch (WrappedException we) { 
        Throwable cause = we.getCause();
        System.out.println(String.format("Failure[%s: %s]", 
                cause.getClass().getName(), cause.getMessage()));
      }
    }
  }}
