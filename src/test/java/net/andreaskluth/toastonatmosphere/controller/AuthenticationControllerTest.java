package net.andreaskluth.toastonatmosphere.controller;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.web.WebAttributes;
import org.springframework.ui.ModelMap;

/**
 * Test suite for {@link AuthenticationController}.
 * 
 * @author Andreas Kluth <mail@andreaskluth.net>
 */
public class AuthenticationControllerTest {

  private static final String EXCEPTION_MESSAGE = "message";

  /**
   * Validate whether the proper page is returned.
   */
  @Test
  public void loginServesLoginPage() {
    Assert.assertEquals("login", controller().login(null, null));
  }

  /**
   * When an error is available add it to the returned model.
   */
  @Test
  public void loginAddsErrorMessageOnFailure() {
    ModelMap model = new ModelMap();

    controller().login(model, httpSessionWithAuthenticationException());

    Assert.assertNotNull(model.get("error"));
    Assert.assertEquals(EXCEPTION_MESSAGE, model.get("error"));
  }

  private HttpSession httpSessionWithAuthenticationException() {
    HttpSession session = Mockito.mock(HttpSession.class);
    Mockito.when(session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).thenReturn(new Exception(EXCEPTION_MESSAGE));
    return session;
  }

  private AuthenticationController controller() {
    return new AuthenticationController();
  }

}
