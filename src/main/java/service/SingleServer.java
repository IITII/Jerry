package service;

import utils.CheckHandler;

import java.util.Properties;

/**
 * @author IITII
 */
public class SingleServer {
    private final Properties serverProp;

    public SingleServer(Properties serverProp) {
        this.serverProp = serverProp;
    }

    public void start() {
        if (CheckHandler.isHttps(serverProp)) {
            httpServer();
        } else {
            httpsServer();
        }
    }

    public void httpServer() {
    }

    public void httpsServer() {
    }
}
