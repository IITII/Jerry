package jn_s17204117.service.handle;


import com.sun.net.httpserver.HttpExchange;
import jn_s17204117.utils.JerryLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author IITII
 */
public class PostHandle extends HttpMethodHandle {

    public PostHandle(Properties properties) {
        super(properties);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);
        byte[] bytes = (
                httpExchange.getRequestURI()
                        + "\n"
                        + httpExchange.getRequestBody()
        ).getBytes();
        httpExchange.sendResponseHeaders(200, bytes.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        JerryLogger.getLogger("")
                .info(logReq(httpExchange));
    }
}
