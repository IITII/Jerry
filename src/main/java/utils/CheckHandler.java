package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author IITII
 */
public class CheckHandler {
    public static Boolean isAutoIndex(Properties properties) {
        String key = "autoindex";
        return properties.getProperty(key) != null;
    }

    public static Boolean isHttps(Properties properties) {
        String[] httpsTag = "ssl.key ssl.crt".split("\\s");
        for (String s : httpsTag) {
            // 空属性
            if (properties.getProperty(s) == null) {
                return false;
            }
            // 文件不存在
            if (!Files.exists(Paths.get(properties.getProperty(s)))) {
                return false;
            }
        }
        return true;
    }
}
