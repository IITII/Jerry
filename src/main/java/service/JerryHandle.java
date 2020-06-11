package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.handle.GetHandle;
import service.handle.PostHandle;
import utils.JerryLogger;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 目前只支持 GET, POST
 *
 * @author IITII
 */
public class JerryHandle implements HttpHandler {
    /**
     * https://blog.csdn.net/u011517841/article/details/79810689
     */
    ThreadPoolExecutor factory = new ThreadPoolExecutor(50,
            Integer.MAX_VALUE / 100,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            (ThreadFactory) Thread::new);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        factory.prestartAllCoreThreads();
        Logger logger = JerryLogger.getLogger("");
        factory.execute(() -> {
            try {
                if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
                    new PostHandle().handle(httpExchange);
                } else if ("GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
                    new GetHandle().handle(httpExchange);
                } else {
                    logger.warning("Nonsupport HTTP Method!");
                }
            } catch (IOException e) {
                logger.severe(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
//        Headers responseHeaders = httpExchange.getResponseHeaders();
//        responseHeaders.set("Content-Type", "text/html;charset=utf-8");
    }
}
