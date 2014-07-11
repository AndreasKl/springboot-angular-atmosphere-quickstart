package net.andreaskluth.toastonatmosphere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Demo controller.
 * 
 * @author Andreas Kluth
 */
@Controller
public class HomeController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home() {
    return "index";
  }
  
}
