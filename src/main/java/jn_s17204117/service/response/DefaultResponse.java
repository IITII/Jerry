package jn_s17204117.service.response;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * 默认 HTTP 状态码响应内容
 *
 * @author IITII
 */
public class DefaultResponse {
    private final static String TEMPLATE
            = "<pre><center><h2>template<hr><p>Jerry-IITII</p></center></pre>";

    private static void commonOperate(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(
                HttpURLConnection.HTTP_NOT_FOUND,
                //Long.parseLong(httpExchange.getRequestHeaders().getFirst("Content-length"))
                response.length()
        );
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
        httpExchange.close();
    }

    public static void code403(HttpExchange httpExchange) throws IOException {
        commonOperate(
                httpExchange,
                TEMPLATE.replace(
                        "template",
                        "403 Forbidden"
                )
        );
    }

    public static void code404(HttpExchange httpExchange) throws IOException {
        commonOperate(
                httpExchange,
                TEMPLATE.replace(
                        "template",
                        "404 NOT FOUND"
                )
        );
    }
}