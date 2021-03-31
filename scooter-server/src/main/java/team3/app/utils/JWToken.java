package team3.app.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Component
public class JWToken {

  private static final String JWT_USERNAME_CLAIM = "sub";
  private static final String JWT_ID_CLAIM = "id";
   static final String JWT_ADMIN_CLAIM = "admin";

  private String userName;
  private Long userId;
  private boolean admin;

  public JWToken() {
  }

  public JWToken(String userName, Long userId, boolean admin) {
    this.userName = userName;
    this.userId = userId;
    this.admin = admin;
  }

  @Value("${jwt.issuer:private company}")
  private String issuer;

  @Value("${jwt.pass-phrase:This is secret info}")
  private String passPhrase;

  @Value("${jwt.duration-of-validity:1200}")
  private int expiration;

  public String encode(Long id, String name, boolean admin) {
    Key key = getKey(passPhrase);

    String token = Jwts.builder()
      .claim(JWT_USERNAME_CLAIM, name)
      .claim(JWT_ID_CLAIM, id)
      .claim(JWT_ADMIN_CLAIM, admin)
      .setIssuer(issuer)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
      .signWith(key, SignatureAlgorithm.HS512)
      .compact();

    return token;
  }

  public static JWToken decode(String token, String passPhrase) {
    try {

      Key key = getKey(passPhrase);

      //validate the token
      Jws<Claims> jws = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token);

      Claims claims = jws.getBody();

      JWToken tk = new JWToken(
        claims.get(JWT_USERNAME_CLAIM).toString(),
        Long.valueOf(claims.get(JWT_ID_CLAIM).toString()),
        (boolean) claims.get(JWT_ADMIN_CLAIM)
      );

      return tk;

    } catch (Exception e) {
      return null;
    }
  }




  private static Key getKey(String passPhrase) {
    byte hmacKey[] = passPhrase.getBytes(StandardCharsets.UTF_8);
    Key key = new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    return key;
  }

}
