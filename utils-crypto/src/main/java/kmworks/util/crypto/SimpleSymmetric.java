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
package kmworks.util.crypto;

import com.google.common.io.BaseEncoding;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author cpl
 */
public class SimpleSymmetric {
  
  private static final String ALGO = "AES";
  private static final Encoder DEFAULT_ENCODER = new Encoder() {
      
      @Override
      public String encode(byte[] plainBytes) {
        return BaseEncoding.base64().encode(plainBytes);
      }

      @Override
      public byte[] decode(String encodedText) {
        return BaseEncoding.base64().decode(encodedText);
      }
    };
  
  private final byte[] keyValue;
  private final Key key;
  private final Cipher cipher;
  private Charset charSet = StandardCharsets.UTF_8;
  
  
  public SimpleSymmetric(String password) {
    keyValue = Hashing.md5(password);
    key = new SecretKeySpec(keyValue, ALGO);
    cipher = getCipher(ALGO);
  }
  
  public SimpleSymmetric(String password, Charset charSet) {
    this(password);
    this.charSet = charSet;
  }
  
  
  public String encrypt(String plainText) {
    return encrypt(plainText, DEFAULT_ENCODER);
  }
  
  public String encrypt(String plainText, Encoder encoder) {
    final byte[] encryptedBytes = encrypt(plainText.getBytes(charSet));
    return encoder.encode(encryptedBytes);
  }
  
  public byte[] encrypt(byte[] plainBytes) {
    prepareCipher(Cipher.ENCRYPT_MODE);
    final byte[] bytes;
    try {
      bytes = cipher.doFinal(plainBytes);
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
      /* THIS SHOULD NEVER HAPPEN */
      return null;      
    }
    return bytes;
  }
  
  public String decrypt(String encryptedEncodedText) {
    return decrypt(encryptedEncodedText, DEFAULT_ENCODER);
  }
  
  public String decrypt(String encryptedEncodedText, Encoder encoder) {
    byte[] encryptedBytes = encoder.decode(encryptedEncodedText);
    return new String(decrypt(encryptedBytes), charSet);
  }
  
  public byte[] decrypt(byte[] encryptedBytes) {
    prepareCipher(Cipher.DECRYPT_MODE);
    final byte[] bytes;
    try {
      bytes = cipher.doFinal(encryptedBytes);
    } catch (IllegalBlockSizeException | BadPaddingException ex) {
      /* THIS SHOULD NEVER HAPPEN */
      return null;      
    }
    return bytes;
  }
  
  private Cipher getCipher(String algo) {
    Cipher result;
    try {
      result = Cipher.getInstance(algo);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
      /* THIS SHOULD NEVER HAPPEN */
      return null;
    }
    return result;
  }
  
  private Cipher prepareCipher(int mode) {
    try {
      cipher.init(mode, key);
    } catch (InvalidKeyException ex) {
      /* THIS SHOULD NEVER HAPPEN */
    }
    return cipher;
  }
  
  public interface Encoder {
    String encode(byte[] plainBytes);
    byte[] decode(String encodedText);
  }
  
}
