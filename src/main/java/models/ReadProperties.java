package models;

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
     * @return HashMap<String.String>
     */
    public Properties getProp() throws IOException {
        Properties properties = loadPropertiesFile();
        if (new CheckProperties().checkProp(properties)) {
            return properties;
        }
        return null;
    }

    /**
     * 加载配置文件
     */
    private Properties loadPropertiesFile() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(path));
        return prop;
    }
}
