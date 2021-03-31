package team3.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import team3.app.rest.AuthenticateController;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

  @Value("${jwt.pass-phrase:This is secret info}")
  private String passPhrase;

  private static final Set<String> SECURE_PATHS = Set.of("/scooters");

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain filterChain) throws ServletException, IOException {

    String servletPath = req.getServletPath();

    //excludes non secure enpoints and option requests
    if (HttpMethod.OPTIONS.matches(req.getMethod()) || SECURE_PATHS.stream().noneMatch(servletPath::startsWith)) {
      filterChain.doFilter(req, res);

      return;
    }

    try {

    JWToken jwToken = null;

      String encryptedToken = req.getHeader(HttpHeaders.AUTHORIZATION);

      if (encryptedToken != null) {
        //remove the prefix
        encryptedToken = encryptedToken.replace("Bearer ", "");

        jwToken = JWToken.decode(encryptedToken, passPhrase);
      }

      if (jwToken == null) {
        throw new AuthenticateController.UnAuthorizedException("You need to login first");
      }

      req.setAttribute(JWToken.JWT_ADMIN_CLAIM, jwToken);
      filterChain.doFilter(req, res);
    } catch (AuthenticateController.UnAuthorizedException e) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//      throw new AuthenticateController.UnAuthorizedException("You need to login first");
    }

  }
}
