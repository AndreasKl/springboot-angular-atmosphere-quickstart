package net.andreaskluth.toastonatmosphere.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller to handle login and log out of users.
 * 
 * @author Andreas Kluth
 */
@Controller
public class AuthenticationController {

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(ModelMap model, HttpSession session) {
    if (hasAuthenticationException(session)) {
      model.put("error", getAuthenticationExceptionMessage(session));
    }
    return "login";
  }

  @RequestMapping(value = "/logout-success", method = RequestMethod.GET)
  public String logout() {
    return "redirect:login";
  }

  private String getAuthenticationExceptionMessage(HttpSession session) {
    if (session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof Exception) {
      Exception ex = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      return ex.getMessage();
    }
    return "Unknown login issue.";
  }

  private boolean hasAuthenticationException(HttpSession session) {
    return session != null && session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null;
  }

}
