package net.andreaskluth.toastonatmosphere.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Test suite for {@link FakeAuthenticationProvider}.
 * 
 * @author Andreas Kluth
 */
public class FakeAuthenticationProviderTest {

  @Test
  public void arbitraryCasedNameReturnsValidAuthentication() {
    AuthenticationProvider provider = createProvider();
    Authentication authentication = provider.authenticate(createAuthenticationTokenWithUserAndPw("AdMiN", "adm1n"));
    Assert.assertNotNull(authentication);
  }

  @Test
  public void validPasswordReturnsValidAuthentication() {
    AuthenticationProvider provider = createProvider();
    Authentication authentication = provider.authenticate(createAuthenticationTokenWithUserAndPw("admin", "adm1n"));
    Assert.assertNotNull(authentication);
  }

  @Test(expected = BadCredentialsException.class)
  public void invalidPasswordRaises() {
    AuthenticationProvider provider = createProvider();
    provider.authenticate(createAuthenticationTokenWithUserAndPw("admin", "admin"));
  }

  @Test(expected = BadCredentialsException.class)
  public void invalidUserRaises() {
    AuthenticationProvider provider = createProvider();
    provider.authenticate(createAuthenticationTokenWithUserAndPw("admon", "adm1n"));
  }

  private AuthenticationProvider createProvider() {
    return new FakeAuthenticationProvider();
  }

  private TestingAuthenticationToken createAuthenticationTokenWithUserAndPw(String username, String password) {
    return new TestingAuthenticationToken(username, password);
  }

}