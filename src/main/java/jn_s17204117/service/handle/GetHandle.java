package jn_s17204117.service.handle;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import jn_s17204117.service.response.DefaultResponse;
import jn_s17204117.utils.UrlToStringPath;
import org.apache.commons.io.FilenameUtils;

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
public class GetHandle extends HttpMethodHandle {
    private Properties properties;

    public GetHandle(Properties properties) {
        super(properties);
        this.properties = properties;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String url = httpExchange.getRequestURI().toString();
        if (url.matches("^/[\\s\\S]*")) {
            Path path = Paths.get(properties.getProperty("root"), url);
            File file = new File(path.toFile().getAbsolutePath());
            if (!file.exists()) {
                DefaultResponse.code404(httpExchange);
                return;
            }
            if (file.isDirectory()) {
                file = new File(
                        Paths.get(properties.getProperty("root"), url, UrlToStringPath.DEFAULT_INDEX)
                                .toFile()
                                .getAbsolutePath()
                );
            }
            Headers headers = httpExchange.getResponseHeaders();
            if (file.isFile()) {
                if (file.getAbsolutePath().matches("[\\s\\S]*\\.(png|jpe?g|gif|svg)(\\?.*)?$")) {
                    headers.set("Content-Type", "image/" + FilenameUtils.getExtension(file.getName()));
                } else {
                    //避免中文乱码，添加头部信息
                    headers.set("Content-Type", "text/html;charset=utf-8");
                }
            }
            httpExchange.sendResponseHeaders(200, file.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            Files.copy(file.toPath(), outputStream);
            //必须手动刷新输出流，并且关闭输出流，要不然会阻塞线程。。。
            outputStream.flush();
            outputStream.close();
        } else {
            DefaultResponse.code404(httpExchange);
        }
        //循环读取
//        File file = new File("E:\\Android\\index.png");
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
//        httpExchange.sendResponseHeaders(200, bufferedInputStream.available());
//        byte[] bytes = new byte[bufferedInputStream.available()];
//        while (bufferedInputStream.available() >= 0) {
//            int status = bufferedInputStream.read(bytes);
//            if (status == -1) {
//                break;
//            }
//        }
//        httpExchange.getResponseBody().write(bytes);
    }
}
