package jn_s17204117.service.handle;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import jn_s17204117.service.response.DefaultResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author IITII
 */
public class AutoIndexHandle extends HttpMethodHandle {
    private final Properties properties;

    public AutoIndexHandle(Properties properties) {
        super(properties);
        this.properties = properties;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        super.handle(httpExchange);
        String index = getIndex(httpExchange, properties);
        Headers headers = httpExchange.getResponseHeaders();
        //避免中文乱码，添加头部信息
        headers.set("Content-Type", "text/html;charset=utf-8");
        if (index != null) {
            httpExchange.sendResponseHeaders(200, index.getBytes().length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(index.getBytes());
            //必须手动刷新输出流，并且关闭输出流，要不然会阻塞线程。。。
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     * 返回index
     *
     * @param httpExchange httpExchange
     * @param properties   变量
     * @return String
     * @throws IOException OutputStream
     */
    public String getIndex(HttpExchange httpExchange, Properties properties) throws IOException {
        Path path = Paths.get(properties.getProperty("root"), httpExchange.getRequestURI().toString());
        if (!Files.exists(path)) {
            DefaultResponse.code404(httpExchange);
            return null;
        }
        if (path.toFile().isFile()) {
            new GetHandle(properties).handle(httpExchange);
//            httpExchange.sendResponseHeaders(200, path.toFile().length());
//            OutputStream outputStream = httpExchange.getResponseBody();
//            Files.copy(path.toFile().toPath(), outputStream);
//            //必须手动刷新输出流，并且关闭输出流，要不然会阻塞线程。。。
//            outputStream.flush();
//            outputStream.close();
            return null;
        } else if (path.toFile().isDirectory()) {
            File[] files = path.toFile().listFiles();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<pre>")
                    .append("<a href=\"../\">../</a>")
                    .append("<br>");
            if (files != null) {
                for (File file : files) {
                    // / 而不是 ./
                    stringBuilder.append("<a href=\"/");
                    if (!"/".equals(httpExchange.getRequestURI().toString())) {
                        stringBuilder.append(
                                httpExchange.getRequestURI()
                                        .toString()
                                        .substring(1)
                        )
                                .append("/");
                    }
                    stringBuilder
                            .append(file.getName())
                            .append("\">")
                            .append(file.getName())
                            .append("</a>")
                            .append("<br>");
                }
            }
            stringBuilder.append("</pre>");
            return stringBuilder.toString();
        } else {
            return "<pre>" + "<a href=\"../\">../</a>" + "</pre>";
        }
    }
}
