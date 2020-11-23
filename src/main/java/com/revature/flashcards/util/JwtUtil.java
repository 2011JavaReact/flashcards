package com.revature.flashcards.util;

import com.google.gson.Gson;
import com.revature.flashcards.model.Auth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public final class JwtUtil {
  private static Gson gson = new Gson();

  private static String claimKey = "auth";

  private static SignatureAlgorithm algo = SignatureAlgorithm.HS256;

  /**
   * @param ttlMillis how long u want jwt to be valid for
   * @param signingKey the string to use for signing.
   * @param auth will be encoded into the jwt token to be decoded on subsequent
   *     requests
   * @return jwt token with the auth object encoded into it.
   */
  public static String create(long ttlMillis, String signingKey, Auth auth) {
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
    Date expires = new Date(nowMillis + ttlMillis);
    byte[] jwtKeyBytes = DatatypeConverter.parseBase64Binary(signingKey);
    Key key = new SecretKeySpec(jwtKeyBytes, algo.getJcaName());

    return Jwts.builder()
        .setIssuedAt(now)
        .setExpiration(expires)
        .claim(claimKey, gson.toJson(auth))
        .signWith(key, algo)
        .compact();
  }

  /**
   * @param jwt the jwt to decode
   * @param signingKey the string used when the jwt was signed
   * @return a nullable Auth parsed from the given jwt
   */
  public static Auth decode(String jwt, String signingKey) {
    try {
      String json = Jwts.parserBuilder()
          .setSigningKey(DatatypeConverter.parseBase64Binary(signingKey))
          .build()
          .parseClaimsJws(jwt).getBody().get(claimKey).toString();
      return gson.fromJson(json, Auth.class);
    } catch (Throwable e) {
      return null;
    }
  }
}
