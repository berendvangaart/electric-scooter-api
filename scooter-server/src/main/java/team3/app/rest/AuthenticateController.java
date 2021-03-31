package team3.app.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import team3.app.utils.JWToken;
import team3.app.models.User;

@RestController
public class AuthenticateController {

  @Autowired
  JWToken tokenGenerator;

  @PostMapping("/authenticate")
  public ResponseEntity<User> authUser(@RequestBody ObjectNode loginData) {

    String mail = loginData.get("email") == null ? null : loginData.get("email").asText();
    String passWord = loginData.get("password") == null? null : loginData.get("password").asText();
    User user = new User();

    if ( mail.substring( 0, mail.indexOf("@")).equals(passWord)){
      user.setId(User.getAutoID());
      user.setEmail(mail);
      user.setName(mail.substring( 0, mail.indexOf("@")));
      user.setHashedPassword(passWord);
      user.setAdmin(false);
    } else {
      throw new UnAuthorizedException("password does not match email name");
    }

    String token = tokenGenerator.encode(user.getId(), user.getName(), user.isAdmin());

    return ResponseEntity.accepted()
      .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
      .body(user);

  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public static class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
      super(message);
    }
  }


}
