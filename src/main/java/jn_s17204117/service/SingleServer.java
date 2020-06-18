package jn_s17204117.service;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import jn_s17204117.utils.CheckHandler;
import jn_s17204117.utils.JerryLogger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
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
    private HttpsServer httpsServer = null;
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
        //为了更快的响应客户端请求，预热所有核心线程
        factory.prestartAllCoreThreads();
        if (CheckHandler.isHttps(serverProp)) {
            httpsServerStart();
        } else {
            httpServerStart();
        }
    }

    public void stop() {
        if (CheckHandler.isHttps(serverProp)) {
            httpsServerStop();
        } else {
            httpServerStop();
        }
    }

    public void httpServerStart() throws IOException {
        String address = serverProp.getProperty("address");
        int port = Integer.parseInt(serverProp.getProperty("port"));
        assert address != null;
        assert port > 0;
        httpServer = HttpServer.create(
                new InetSocketAddress(
                        InetAddress.getByName(address),
                        port
                ),
                0);
        httpServer.createContext("/", new JerryHandle(factory, serverProp));
        httpServer.start();
    }

    public void httpServerStop() {
        httpServer.stop(0);
    }

    public void httpsServerStart() {
        JerryLogger logger = new JerryLogger();
        try {
            InetSocketAddress address = new InetSocketAddress(
                    InetAddress.getByName(serverProp.getProperty("address")),
                    Integer.parseInt(serverProp.getProperty("port"))
            );
            httpsServer = HttpsServer.create(address, 0);
            KeyStore keyStore = KeyStore.getInstance(serverProp.getProperty("ssl.type", "jks"));
            keyStore.load(new FileInputStream(serverProp.getProperty("ssl.file")), serverProp.getProperty("ssl.password").toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, serverProp.getProperty("ssl.password").toCharArray());
            SSLContext context = SSLContext.getInstance(System.getProperty("ssl.protocol", "TLSv1.2"));
            context.init(keyManagerFactory.getKeyManagers(), null, null);
//
//            HttpsServer httpsServer = HttpsServer.create(address, 0);
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//            // Initialise the keystore
//            char[] password = serverProp.getProperty("ssl.password").toCharArray();
//            KeyStore ks = KeyStore.getInstance("JKS");
//            FileInputStream fis = new FileInputStream(serverProp.getProperty("ssl.crt"));
//            ks.load(fis, password);
//
//            // Set up the key manager factory
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//            kmf.init(ks, password);
//
//            // Set up the trust manager factory
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
//            tmf.init(ks);
//
//            // Set up the HTTPS context and parameters
//            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            httpsServer.setHttpsConfigurator(new HttpsConfigurator(context) {
                @Override
                public void configure(HttpsParameters params) {
                    try {
                        // Initialise the SSL context
                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());

                        // Get the default parameters
                        SSLParameters sslParameters = c.getDefaultSSLParameters();
                        params.setSSLParameters(sslParameters);
                    } catch (Exception ex) {
                        logger.severe(ex.getLocalizedMessage());
                        logger.severe("Failed to create HTTPS port");
                    }
                }
            });
            httpsServer.createContext("/", new JerryHandle(factory, serverProp));
            httpsServer.start();
        } catch (Exception exception) {
            logger.severe(exception.getLocalizedMessage());
            exception.printStackTrace();
        }
        logger.close();
    }

    public void httpsServerStop() {
        httpsServer.stop(0);
    }
}
