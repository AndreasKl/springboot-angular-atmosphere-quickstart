package net.andreaskluth.toastonatmosphere.websocket;

import net.andreaskluth.toastonatmosphere.Charsets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.Get;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.springframework.stereotype.Component;

/**
 * Provides a mean for visitors to register to subscribe to the "toast"
 * messages.
 * 
 * @author Andreas Kluth
 */
@Component
@ManagedService(path = "/websocket/toast")
public class ToastService {

  private final Log log = LogFactory.getLog(getClass());

  @Get
  public void init(AtmosphereResource resource) {
    resource.getResponse().setCharacterEncoding(Charsets.UTF_8);
  }

  @Ready
  public void onReady(final AtmosphereResource resource) {
    log.info("Browser " + resource.uuid() + " connected.");
  }

  @Disconnect
  public void onDisconnect(AtmosphereResourceEvent event) {
    if (event.isCancelled()) {
      log.info("Browser " + event.getResource().uuid() + " unexpectedly disconnected");
    } else if (event.isClosedByClient()) {
      log.info("Browser " + event.getResource().uuid() + " closed the connection");
    }
  }

}
