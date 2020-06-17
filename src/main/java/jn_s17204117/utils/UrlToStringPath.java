package jn_s17204117.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author IITII
 */
public class UrlToStringPath {
    public final static String DEFAULT_INDEX = "index.html";

    public static int responseCode(String url, String root) {
        if ("".equals(url) || url == null) {
            return 404;
        }
        if (url.matches("^/[\\s\\S]*")) {
            Path path = Paths.get(root, url);
            if (path.isAbsolute()) {
                if (url.matches("[\\s\\S]*/$")) {
                    if (Files.exists(Paths.get(path.toAbsolutePath().toString(), DEFAULT_INDEX))) {
                        return 200;
                    } else {
                        return 404;
                    }
                } else {
                    if (Files.exists(path)) {
                        return 200;
                    } else {
                        return 404;
                    }
                }
            } else {
                return 404;
            }
        } else {
            return 403;
        }
    }

    public static String urlToPathString(String url) {
        return url;
    }
}
