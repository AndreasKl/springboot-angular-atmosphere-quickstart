package net.andreaskluth.toastonatmosphere.configuration;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import net.andreaskluth.toastonatmosphere.websocket.ToastService;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.MetaBroadcaster;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 * Configures an {@link AtmosphereServlet} and announces it to the
 * {@link ServletContext}.
 * 
 * @author Andreas Kluth
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    configureAthmosphere(servletContext);
  }

  @Bean
  public MetaBroadcaster metaBroadcaster(AtmosphereFramework atmosphereFramework) {
    return atmosphereFramework.metaBroadcaster();
  }

  @Bean
  public AtmosphereFramework atmosphereFramework() {
    return new NoAnalyticsAtmosphereFramework();
  }

  private void configureAthmosphere(ServletContext servletContext) {
    AtmosphereServlet servlet = new AtmosphereServlet();
    Field frameworkField = ReflectionUtils.findField(AtmosphereServlet.class, "framework");
    ReflectionUtils.makeAccessible(frameworkField);
    ReflectionUtils.setField(frameworkField, servlet, atmosphereFramework());

    ServletRegistration.Dynamic atmosphereServlet = servletContext.addServlet("atmosphereServlet", servlet);
    atmosphereServlet.setInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, ToastService.class.getPackage().getName());
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_CACHE, UUIDBroadcasterCache.class.getName());
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_SHARABLE_THREAD_POOLS, "true");
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_MESSAGE_PROCESSING_THREADPOOL_MAXSIZE, "10");
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_ASYNC_WRITE_THREADPOOL_MAXSIZE, "10");

    servletContext.addListener(new org.atmosphere.cpr.SessionSupport());
    atmosphereServlet.addMapping("/websocket/*");
    // Set the loadOnStartup priority to sth. higher than -1 to avoid lazy
    // instantiation which would cause issues with injecting the
    // BroadcasterFactory.
    atmosphereServlet.setLoadOnStartup(0);
    atmosphereServlet.setAsyncSupported(true);
  }

  /**
   * Disables the analytics functionality of atmosphere otherwise it would
   * contact google analytics.
   */
  public class NoAnalyticsAtmosphereFramework extends AtmosphereFramework {

    /**
     * Creates a new instance of {@link NoAnalyticsAtmosphereFramework}.
     */
    public NoAnalyticsAtmosphereFramework() {
      super();
    }

    @Override
    protected void analytics() {
      // NOOP
    }

  }

}
