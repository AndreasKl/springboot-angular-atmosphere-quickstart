package net.andreaskluth.toastonatmosphere.controller;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

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

  @RequestMapping(value = "/broadcast/{message}", method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public void broadcast(@PathVariable("message") String message) {
    Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup("/websocket/toast", false);
    if (broadcaster != null) {
      broadcaster.broadcast(message);
    }
  }

}
