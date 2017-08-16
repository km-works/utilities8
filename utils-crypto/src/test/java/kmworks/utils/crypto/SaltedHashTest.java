/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmworks.utils.crypto;

import kmworks.util.crypto.SaltedHash;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author cpl
 */
public class SaltedHashTest {
  
  public SaltedHashTest() {
  }

  @Test
  public void testGenerate_Validate() throws Exception {
    final String plainValue = "abraxa5";
    final String saltedHash = SaltedHash.generate(plainValue);
    assertTrue(SaltedHash.validate(plainValue, saltedHash));
  }

}
