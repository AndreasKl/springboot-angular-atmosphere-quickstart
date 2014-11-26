package net.andreaskluth.toastonatmosphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application root, to disable the thymeleaf template cache run the application
 * with 'dev' profile:
 * <p>
 * <code>-Dspring.profiles.active=dev</code>.
 * 
 * @author Andreas Kluth
 */
@EnableAutoConfiguration
@ComponentScan
public class Application {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.run(args);
  }

}
