package security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * In-memory fake authentication provider.
 * 
 * @author Andreas Kluth
 */
@Component
public class FakeAuthenticationProvider implements AuthenticationProvider {

  private static final String HARD_CODED_SUPER_SECRET_NAME = "admin";

  private static final String HARD_CODED_SUPER_SECRET_PW = "adm1n";

  private final String saltedAndHashedSecret;

  private final PasswordEncoder passwordEncoder;

  /**
   * Creates a new instance of {@link FakeAuthenticationProvider}.
   * 
   * @param encoder
   *          to hash and salt passwords.
   */
  public FakeAuthenticationProvider(PasswordEncoder passwordEncoder) {
    if (passwordEncoder == null) {
      throw new NullPointerException("passwordEncoder must not be null");
    }
    this.passwordEncoder = passwordEncoder;
    this.saltedAndHashedSecret = passwordEncoder.encode(HARD_CODED_SUPER_SECRET_PW);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();

    if (isValidUser(name, password)) {
      List<GrantedAuthority> grantedAuths = new ArrayList<>();
      grantedAuths.add(new SimpleGrantedAuthority("USER"));
      return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
    }

    throw new BadCredentialsException("Invalid password or user name.");
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  private boolean isValidUser(String name, String password) {
    return HARD_CODED_SUPER_SECRET_NAME.equalsIgnoreCase(name)
        && passwordEncoder.matches(password, saltedAndHashedSecret);
  }

}
