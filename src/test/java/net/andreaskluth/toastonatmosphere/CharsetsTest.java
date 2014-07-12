package net.andreaskluth.toastonatmosphere;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test suite for {@link Charsets}
 * 
 * @author Andreas Kluth
 */
public class CharsetsTest {

  @Test
  public void validateCharsetNameForUtf8() {
    assertEquals("UTF-8", Charsets.UTF_8);
  }

}
