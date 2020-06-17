package jn_s17204117.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 由于安全问题， 这里不采用 SimpleDateFormat
 *
 * @author IITII
 */
public class JerryDate {
    private final ZonedDateTime date = ZonedDateTime.now();
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    /**
     * 线程安全
     *
     * @see <a href="https://stackoverflow.com/questions/6840803/why-is-javas-simpledateformat-not-thread-safe">
     * Why is Java's SimpleDateFormat not thread-safe?</a>
     */
    private final DateTimeFormatter dtf;

    public JerryDate(String dateFormat) {
        this.dateFormat = dateFormat;
        dtf = DateTimeFormatter.ofPattern(dateFormat);
    }

    public JerryDate() {
        dtf = DateTimeFormatter.ofPattern(dateFormat, Locale.US);
    }

    public String getDateString() {
        return dtf.format(date);
    }

    /**
     * 规范日志文件名.
     * 比如 2020-6-10-12-12-00.log
     *
     * @return String 文件名
     */
    public String getLogName() {
        return dtf.format(date).replaceAll("[ :]", "-") + ".log";
    }
}
