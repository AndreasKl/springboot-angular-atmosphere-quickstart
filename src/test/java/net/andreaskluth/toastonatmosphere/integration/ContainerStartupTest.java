package net.andreaskluth.toastonatmosphere.integration;

import net.andreaskluth.toastonatmosphere.Application;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Validates if the container starts up and the configuration is valid.
 * 
 * @author Andreas Kluth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ContainerStartupTest implements ApplicationContextAware {

  private ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    this.context = context;
  }

  @Test
  public void contextIsInitialized() {
    Assert.assertNotNull(context);
  }

}
