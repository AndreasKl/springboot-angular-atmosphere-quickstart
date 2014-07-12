package net.andreaskluth.toastonatmosphere.configuration;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.atmosphere.cache.UUIDBroadcasterCache;
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
    configureAthmosphere(servletContext);
  }

  @Bean
  public MetaBroadcaster metaBroadcaster() {
    return MetaBroadcaster.getDefault();
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

  private void configureAthmosphere(ServletContext servletContext) {
    AtmosphereServlet servlet = new AtmosphereServlet();
    Field frameworkField = ReflectionUtils.findField(AtmosphereServlet.class, "framework");
    ReflectionUtils.makeAccessible(frameworkField);
    ReflectionUtils.setField(frameworkField, servlet, new NoAnalyticsAtmosphereFramework());

    ServletRegistration.Dynamic atmosphereServlet = servletContext.addServlet("atmosphereServlet", servlet);
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.packages", "net.andreaskluth.toastonatmosphere.websocket");
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcasterCacheClass", UUIDBroadcasterCache.class.getName());
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcaster.shareableThreadPool", "true");
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcaster.maxProcessingThreads", "10");
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcaster.maxAsyncWriteThreads", "10");
    atmosphereServlet.setInitParameter("org.atmosphere.useComet", "false");
    servletContext.addListener(new org.atmosphere.cpr.SessionSupport());
    atmosphereServlet.addMapping("/websocket/*");
    // Set the loadOnStartup priority to sth. higher than -1 to avoid lazy instantiation which
    // would cause issues with injecting the BroadcasterFactory.
    atmosphereServlet.setLoadOnStartup(0);
    atmosphereServlet.setAsyncSupported(true);
  }

  public class NoAnalyticsAtmosphereFramework extends AtmosphereFramework {

    public NoAnalyticsAtmosphereFramework() {
      super();
    }

    @Override
    protected void analytics() {
    }

  }

}
