package jn_s17204117.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jn_s17204117.service.handle.AutoIndexHandle;
import jn_s17204117.service.handle.GetHandle;
import jn_s17204117.service.handle.PostHandle;
import jn_s17204117.utils.JerryLogger;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 目前只支持 GET, POST
 *
 * @author IITII
 */
public class JerryHandle implements HttpHandler {
    private final ThreadPoolExecutor factory;
    private final Properties properties;

    public JerryHandle(ThreadPoolExecutor factory, Properties properties) {
        this.factory = factory;
        this.properties = properties;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        JerryLogger logger = JerryLogger.getJerryLogger();
        factory.execute(() -> {
            try {
                if (Boolean.parseBoolean(properties.getProperty("autoindex"))) {
                    new AutoIndexHandle(properties).handle(httpExchange);
                    return;
                }
                if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
                    new PostHandle(properties).handle(httpExchange);
                } else if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
                    new GetHandle(properties).handle(httpExchange);
                } else {
                    logger.warning("Nonsupport HTTP Method!");
                }
            } catch (IOException e) {
                logger.severe(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }
}
