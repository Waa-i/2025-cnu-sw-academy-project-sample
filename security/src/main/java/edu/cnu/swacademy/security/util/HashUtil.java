package edu.cnu.swacademy.security.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashUtil {
  public static String sha512(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

      return Base64.getEncoder().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-512 Algorithm not found", e);
    }
  }
}
