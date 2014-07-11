package net.andreaskluth.toastonatmosphere;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Application root, to disable the thymeleaf template cache run the application
 * with debug arguments.
 * 
 * @author Andreas Kluth
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.setDefaultProperties(getPropertiesOverrides());
    app.setShowBanner(false);
    app.run(args);
  }

  private static Properties getPropertiesOverrides() {
    Properties defaultProperties = new Properties();
    if (System.getenv("debug") != null) {
      defaultProperties.put("spring.thymeleaf.cache", false);
    }
    defaultProperties.put("spring.thymeleaf.mode", "LEGACYHTML5");
    return defaultProperties;
  }

}
