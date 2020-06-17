package jn_s17204117.service.handle;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Properties;

/**
 * @author IITII
 */
public class AutoIndexHandle extends HttpMethodHandle {
    public AutoIndexHandle(Properties properties) {
        super(properties);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);

        StringBuilder stringBuilder = new StringBuilder();

        httpExchange.sendResponseHeaders(200, 0);
    }
}
