package jn_s17204117.models;

import jn_s17204117.utils.JerryLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * 读取 Properties 文件
 *
 * @author IITII
 */
public class ReadProperties {
    private final String path;

    public ReadProperties(String path) {
        this.path = path;
    }

    /**
     * 校验配置文件，并获取参数
     *
     * @return properties
     */
    public Properties getProp() throws IOException {
        Properties properties = loadPropertiesFile();
        if (new CheckProperties().checkProp(properties)) {
            return properties;
        }
        JerryLogger.getJerryLogger().severe("Error conf. Exiting...");
        //Thread.currentThread().interrupt();
        System.exit(1);
        return null;
    }

    /**
     * 加载配置文件
     *
     * @throws IOException Properties 加载异常
     */
    public Properties loadPropertiesFile() throws IOException {
        Properties prop = new Properties();
        prop.load(
                new FileInputStream(new File(path).getAbsolutePath())
        );
        return prop;
    }
}
