package com.revature.flashcards.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
  public static String hash(String plainTextPass) {
    return BCrypt.hashpw(plainTextPass, BCrypt.gensalt(12));
  }

  public static boolean doesMatch(String plainTextPass, String hashedPass) {
    return BCrypt.checkpw(plainTextPass, hashedPass);
  }
}
