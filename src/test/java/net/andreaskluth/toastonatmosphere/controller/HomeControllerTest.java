package net.andreaskluth.toastonatmosphere.controller;

import static org.junit.Assert.assertEquals;
import net.andreaskluth.toastonatmosphere.websocket.ToastService;

import org.atmosphere.cpr.MetaBroadcaster;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test suite for {@link HomeController}
 * 
 * @author Andreas Kluth
 */
public class HomeControllerTest {

  private final HomeController controller() {
    return controller(Mockito.mock(MetaBroadcaster.class));
  }

  private final HomeController controller(MetaBroadcaster broadcaster) {
    return new HomeController(broadcaster);
  }

  /**
   * The home action should return the index page.
   */
  @Test
  public void homeServesIndexPage() {
    assertEquals("index", controller().home());
  }

  /**
   * The broadcast should publish to the broadcaster.
   */
  @Test
  public void broadcastPublishedMessageToBroadcaster() {
    MetaBroadcaster metaBroadcaster = Mockito.mock(MetaBroadcaster.class);
    controller(metaBroadcaster).broadcast("Hallo Welt");
    Mockito.verify(metaBroadcaster).broadcastTo(ToastService.PATH, "Hallo Welt");
  }

}
