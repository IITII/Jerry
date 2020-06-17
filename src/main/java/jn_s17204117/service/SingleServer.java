package jn_s17204117.service;

import com.sun.net.httpserver.HttpServer;
import jn_s17204117.utils.CheckHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class SingleServer {
    private final Properties serverProp;
    private HttpServer httpServer = null;
    /**
     * https://blog.csdn.net/u011517841/article/details/79810689
     */
    ThreadPoolExecutor factory = new ThreadPoolExecutor(50,
            Integer.MAX_VALUE / 100,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            (ThreadFactory) Thread::new);

    public SingleServer(Properties serverProp) {
        this.serverProp = serverProp;
    }

    public void start() throws IOException {
        if (CheckHandler.isHttps(serverProp)) {
            httpsServerStart();
        } else {
            httpServerStart();
        }
    }

    public void stop() {
        if (CheckHandler.isHttps(serverProp)) {
            httpServerStop();
        } else {
            httpsServerStop();
        }
    }

    public void httpServerStart() throws IOException {
        //为了更快的响应客户端请求，预热所有核心线程
        factory.prestartAllCoreThreads();
        String address = serverProp.getProperty("address");
        int port = Integer.parseInt(serverProp.getProperty("port"));
        assert address != null;
        assert port > 0;
        httpServer = HttpServer.create(
                new InetSocketAddress(
                        InetAddress.getByName(address),
                        port
                ),
                Integer.MAX_VALUE / 100);
        httpServer.createContext("/", new JerryHandle(factory, serverProp));
        httpServer.start();
    }

    public void httpServerStop() {
        httpServer.stop(0);
    }

    public void httpsServerStart() {
    }

    public void httpsServerStop() {
    }
}
