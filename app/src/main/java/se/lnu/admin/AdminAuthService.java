package se.lnu.admin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AdminAuthService {

  private static final String ADMIN_USERNAME = "admin";

  /*
   * Demo password: Admin@2026
   * The actual password is not stored directly in the login screen.
   */
  private static final String PASSWORD_SALT = "LNU_KIOSK_ADMIN_SALT_2026";

  private static final String ADMIN_PASSWORD_HASH =
          "52f9e9189f75d2897bf443a75fa628df915c2d6a27ffe41b04c8f4f2137d1c01";

  private AdminAuthService() {
    // Utility class
  }

  public static boolean isValidLogin(String username, String password) {
    if (username == null || password == null) {
      return false;
    }

    String cleanedUsername = username.trim();

    if (!cleanedUsername.equals(ADMIN_USERNAME)) {
      return false;
    }

    String enteredPasswordHash = hashPassword(password);

    return ADMIN_PASSWORD_HASH.equals(enteredPasswordHash);
  }

  private static String hashPassword(String password) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      String saltedPassword = PASSWORD_SALT + password;

      byte[] hashBytes = digest.digest(
              saltedPassword.getBytes(StandardCharsets.UTF_8)
      );

      StringBuilder hexString = new StringBuilder();

      for (byte b : hashBytes) {
        String hex = Integer.toHexString(0xff & b);

        if (hex.length() == 1) {
          hexString.append('0');
        }

        hexString.append(hex);
      }

      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 algorithm not available", e);
    }
  }
}