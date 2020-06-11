package service.handle;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.JerryDate;

import java.io.IOException;


/**
 * @author IITII
 */
public class HttpMethodHandle implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }

    /**
     * 日志消息，nginx 风格
     * '$remote_addr - [$time_local] ' '"$request" $status' ' "$http_user_agent"'
     *
     * @param httpExchange HttpExchange
     */
    public String logMsg(HttpExchange httpExchange) {

        return httpExchange.getRemoteAddress() +
                "-" +
                new JerryDate().getDateString() +
                "-" +
                httpExchange.getRequestMethod() +
                "-" +
                httpExchange.getRequestHeaders();

    }
}
