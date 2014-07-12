package net.andreaskluth.toastonatmosphere;

import java.util.Arrays;
import java.util.Properties;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
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

  @Bean
  public TomcatEmbeddedServletContainerFactory tomcatContainerFactory() {
    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
    factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[] {
      tomcatContextCustomizer()
    }));
    return factory;
  }

  @Bean
  public TomcatContextCustomizer tomcatContextCustomizer() {
    return new TomcatContextCustomizer() {
      @Override
      public void customize(Context context) {
        context.addServletContainerInitializer(new WsSci(), null);
      }
    };
  }

  private static Properties getPropertiesOverrides() {
    Properties defaultProperties = new Properties();
    if (System.getenv("debug") != null) {
      defaultProperties.put("spring.thymeleaf.cache", false);
    }
    defaultProperties.put("server.session-timeout", 10);
    defaultProperties.put("spring.thymeleaf.mode", "LEGACYHTML5");
    return defaultProperties;
  }

}
