package jn_s17204117.service.handle;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jn_s17204117.utils.JerryDate;
import jn_s17204117.utils.JerryLogger;

import java.io.IOException;
import java.util.Properties;


/**
 * @author IITII
 */
public class HttpMethodHandle implements HttpHandler {

    public HttpMethodHandle(Properties properties) {
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        JerryLogger logger = new JerryLogger();
        logger.info(logReq(httpExchange));
        logger.close();
    }

    /**
     * 日志消息，nginx 风格
     * '$remote_addr - [$time_local] ' '"$request" $status' ' "$http_user_agent"'
     *
     * @param httpExchange HttpExchange
     * @return String 访问日志消息
     */
    public String logReq(HttpExchange httpExchange) {

        return httpExchange.getRemoteAddress() +
                ", " +
                new JerryDate().getDateString() +
                ", " +
                httpExchange.getRequestMethod() +
                ", " +
                httpExchange.getRequestURI();

    }
}
