package service.handle;

import javax.xml.ws.spi.http.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * 默认 HTTP 状态码响应内容
 *
 * @author IITII
 */
public class DefaultResponse {
    public static void code404(HttpExchange httpExchange) throws IOException {
        httpExchange.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
        OutputStream os = httpExchange.getResponseBody();
        os.write("<center><h2>404</h2><hr><p>Jerry-IITII</p></center>".getBytes());
        os.close();
        httpExchange.close();
    }
}