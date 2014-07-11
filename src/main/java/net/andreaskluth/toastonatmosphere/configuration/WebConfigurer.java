package net.andreaskluth.toastonatmosphere.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.cpr.AtmosphereServlet;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * Configures an {@link AtmosphereServlet} and announces it to the {@link ServletContext}. 
 *  
 * @author Andreas Kluth
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    configureAthmosphere(servletContext);
  }

  private void configureAthmosphere(ServletContext servletContext) {
    AtmosphereServlet servlet = new AtmosphereServlet();
    ServletRegistration.Dynamic atmosphereServlet = servletContext.addServlet("atmosphereServlet", servlet);

    atmosphereServlet.setInitParameter("org.atmosphere.cpr.packages", "net.andreaskluth.toastonatmosphere.websocket");
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcasterCacheClass",
      UUIDBroadcasterCache.class.getName());
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcaster.shareableThreadPool", "true");
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcaster.maxProcessingThreads", "10");
    atmosphereServlet.setInitParameter("org.atmosphere.cpr.broadcaster.maxAsyncWriteThreads", "10");
    atmosphereServlet.setInitParameter("org.atmosphere.useComet", "false");
    servletContext.addListener(new org.atmosphere.cpr.SessionSupport());

    atmosphereServlet.addMapping("/websocket/*");
    atmosphereServlet.setLoadOnStartup(10);
    atmosphereServlet.setAsyncSupported(true);
  }

}
