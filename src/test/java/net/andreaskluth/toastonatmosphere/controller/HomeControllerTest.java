package net.andreaskluth.toastonatmosphere.controller;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test suite for {@link HomeController}
 * 
 * @author Andreas Kluth
 */
public class HomeControllerTest {

  private final HomeController controller = new HomeController();

  /**
   * The home controller should return the index page.
   */
  @Test
  public void homeServesIndexPage() {
    assertEquals("index", controller.home());
  }

}
