/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmworks.utils.crypto;

import kmworks.util.crypto.SimpleSymmetric;
import com.google.common.io.BaseEncoding;
import org.junit.Test;
import static org.junit.Assert.*;
import kmworks.util.crypto.SimpleSymmetric.Encoder;

/**
 *
 * @author cpl
 */
public class SimpleSymmetricTest {
  
  public SimpleSymmetricTest() {
  }

  @Test
  public void testEncryptDecrypt_String() {
    final String testString = "1234567890_TEST_string";
    final String password = "abrakadabrah";
    SimpleSymmetric encryptor = new SimpleSymmetric(password);
    final String encryptedEncodedString = encryptor.encrypt(testString);
    System.out.println("encrypted+Base64-encoded: " + encryptedEncodedString);
    final String decryptedPlainString = encryptor.decrypt(encryptedEncodedString);
    assertEquals(testString, decryptedPlainString);
  }
  
  @Test
  public void testEncryptDecrypt_String2() {
    final String testString = "1234567890_TEST_string";
    final String password = "abrakadabrah";
    SimpleSymmetric encryptor = new SimpleSymmetric(password);
    
    Encoder encoder = new Encoder() {
      
      @Override
      public String encode(byte[] plainBytes) {
        return BaseEncoding.base64Url().encode(plainBytes);
      }

      @Override
      public byte[] decode(String encodedText) {
        return BaseEncoding.base64Url().decode(encodedText);
      }
    };
    
    final String encryptedEncodedString = encryptor.encrypt(testString, encoder);
    System.out.println("encrypted+Base64URL-encoded: " + encryptedEncodedString);
    final String decryptedPlainString = encryptor.decrypt(encryptedEncodedString, encoder);
    assertEquals(testString, decryptedPlainString);    
  }

}
