package net.andreaskluth.toastonatmosphere.controller;

import net.andreaskluth.toastonatmosphere.websocket.ToastService;

import org.atmosphere.cpr.MetaBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * Demo controller.
 * 
 * @author Andreas Kluth
 */
@Controller
public class HomeController {

  private final MetaBroadcaster broadcaster;

  /**
   * Creates a new instance of {@link HomeController}.
   * 
   * @param metaBroadcaster
   *          the atmosphere meta broadcaster.
   */
  @Autowired
  public HomeController(MetaBroadcaster metaBroadcaster) {
    if (metaBroadcaster == null) {
      throw new NullPointerException("metaBroadcaster must not be null");
    }
    this.broadcaster = metaBroadcaster;
  }

  /**
   * Home action.
   * 
   * @return the index page.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home() {
    return "index";
  }

  /**
   * Broadcast a message to all registered clients.
   * 
   * @param message
   *          to broadcast.
   * @throws InterruptedException
   */
  @HystrixCommand(commandProperties = {
      @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "100"),
      @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true") })
  @RequestMapping(value = "/broadcast/{message}", method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public void broadcast(@PathVariable("message") String message) {
    broadcaster.broadcastTo(ToastService.PATH, message);
  }

  public void fallback(String message) {
    broadcaster.broadcastTo(ToastService.PATH, "fallback: " + message);
  }

}
