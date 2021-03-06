package jn_s17204117.utils;

import jn_s17204117.models.ReadProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author IITII
 */
public class JerryLogger {
    private final static String PROP_FILENAME = "/conf/logging.properties";
    private final static String LOG_LEVEL = "INFO";
    private static FileHandler fileHandler = null;
    private static Logger logger;

    // Single Instance

    private volatile static JerryLogger jerryLogger;

    private JerryLogger(String loggerName) {
        logger = getLogger(loggerName);
    }

    public static JerryLogger getJerryLogger() {
        if (jerryLogger == null) {
            synchronized (JerryLogger.class) {
                if (jerryLogger == null) {
                    jerryLogger = new JerryLogger("");
                }
            }
        }
        return jerryLogger;
    }

    public static JerryLogger getJerryLogger(String loggerName) {
        if (jerryLogger == null) {
            synchronized (JerryLogger.class) {
                if (jerryLogger == null) {
                    jerryLogger = new JerryLogger(loggerName);
                }
            }
        }
        return jerryLogger;
    }

    /**
     * 查找或者创建一个 logger
     * <p>
     * logger类型       对应的整数
     * OFF              最大整数（ Integer. MAX_VALUE）
     * SEVERE           1000
     * WARNING          900
     * INFO             800
     * CONFIG           700
     * FINE             500
     * FINER            400
     * FINEST           300
     * ALL              最小整数（Integer. MIN_VALUE）
     *
     * @param loggerName logger 名称
     *
     * @return Logger
     */
    private Logger getLogger(String loggerName) {
        try {
            Properties properties = new ReadProperties(
                    JerryLogger.class.getResource(PROP_FILENAME).getPath()
            ).loadPropertiesFile();

            //获取日志器名称，默认为 Jerry
            Logger logger = Logger.getLogger(
                    "".equals(loggerName)
                            ? properties.getOrDefault("log.name", "Jerry").toString()
                            : loggerName
            );
            logger.setLevel(Level.parse(properties.getProperty("log.level", LOG_LEVEL)));
            //追加日志记录
            fileHandler = new FileHandler(
                    //设置日志文件文件名
                    getOrCreateLogFile(new JerryDate("yyyy-MM-dd").getLogName())
                            .toAbsolutePath()
                            .toString(),
                    true
            );
            // 设置日志格式
            fileHandler.setFormatter(
                    (Formatter) new GetObjectByStringValue(
                            properties.getOrDefault("log.formatter", "java.util.logging.SimpleFormatter")
                                    .toString()
                    ).getNewInstance()
            );
            logger.addHandler(fileHandler);
            return logger;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public void close() {
        jerryLogger = null;
        if (fileHandler != null) {
            // 关闭 fileHandler 避免重复创建日志文件
            fileHandler.close();
        }
    }

    private static Path getOrCreateLogFile(String filename) throws IOException {
        Properties properties = new ReadProperties(
                JerryLogger.class.getResource(PROP_FILENAME).getPath()
        ).loadPropertiesFile();
        Path configFilePath = Paths.get(
                properties.getOrDefault("log.path", "").toString()
        );
//        Path configFilePath = Paths.get(
//                JerryLogger.class.getResource(PROP_FILENAME).getPath(),
//                properties.getOrDefault("log.path", "").toString()
//        );
        //若日志文件夹不存在，直接创建
        if (!Files.exists(configFilePath)) {
            Files.createDirectory(configFilePath);
        }
        return Paths.get(
                configFilePath.toAbsolutePath().toString(),
                filename
        );
    }

    /**
     * Log an INFO message.
     * <p>
     * If the logger is currently enabled for the INFO message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     *
     * @param msg The string message (or a key in the message catalog)
     */
    public void info(String msg) {
        logger.info(msg);
    }

    /**
     * Log an INFO message.
     * <p>
     * If the logger is currently enabled for the INFO message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     *
     * @param msg The string message (or a key in the message catalog)
     */
    public void warning(String msg) {
        logger.warning(msg);
    }

    /**
     * Log an INFO message.
     * <p>
     * If the logger is currently enabled for the INFO message
     * level then the given message is forwarded to all the
     * registered output Handler objects.
     * <p>
     *
     * @param msg The string message (or a key in the message catalog)
     */
    public void severe(String msg) {
        logger.severe(msg);
    }
}
