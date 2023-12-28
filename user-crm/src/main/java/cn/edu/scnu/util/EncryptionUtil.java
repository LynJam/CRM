package cn.edu.scnu.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncryptionUtil {

  public static String sha(String inStr) {
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
      byte[] md5Bytes = sha.digest(byteArray);
      StringBuilder hexValue = new StringBuilder();
      for (byte md5Byte : md5Bytes) {
        int val = ((int) md5Byte) & 0xff;
        if (val < 16) {
          hexValue.append("0");
        }
        hexValue.append(Integer.toHexString(val));
      }
      return hexValue.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
