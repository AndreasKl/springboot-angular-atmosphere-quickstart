package net.andreaskluth.toastonatmosphere.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import net.andreaskluth.toastonatmosphere.websocket.ToastService;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereFramework;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.MetaBroadcaster;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
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
    configureAthmosphere(atmosphereFramework(), servletContext);
  }

  @Bean
  public TomcatEmbeddedServletContainerFactory tomcatContainerFactory() {
    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
    factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[] { tomcatContextCustomizer() }));
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

  @Bean
  public MetaBroadcaster metaBroadcaster() {
    AtmosphereFramework framework = atmosphereFramework();
    return framework.metaBroadcaster();
  }

  @Bean
  public AtmosphereFramework atmosphereFramework() {
    return new NoAnalyticsAtmosphereFramework();
  }

  private void configureAthmosphere(AtmosphereFramework framework, ServletContext servletContext) {
    AtmosphereServlet servlet = new AtmosphereServlet();
    Field frameworkField = ReflectionUtils.findField(AtmosphereServlet.class, "framework");
    ReflectionUtils.makeAccessible(frameworkField);
    ReflectionUtils.setField(frameworkField, servlet, framework);

    ServletRegistration.Dynamic atmosphereServlet = servletContext.addServlet("atmosphereServlet", servlet);
    atmosphereServlet.setInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, ToastService.class.getPackage().getName());
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_CACHE, UUIDBroadcasterCache.class.getName());
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_SHARABLE_THREAD_POOLS, "true");
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_MESSAGE_PROCESSING_THREADPOOL_MAXSIZE, "10");
    atmosphereServlet.setInitParameter(ApplicationConfig.BROADCASTER_ASYNC_WRITE_THREADPOOL_MAXSIZE, "10");

    // FIXME: Adding makes the application work.
    // atmosphereServlet.setInitParameter(ApplicationConfig.JSR356_MAPPING_PATH,
    // "/websocket/*");

    servletContext.addListener(new org.atmosphere.cpr.SessionSupport());
    atmosphereServlet.addMapping("/websocket/*");
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
