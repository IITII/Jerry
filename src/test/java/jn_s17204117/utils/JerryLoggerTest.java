package jn_s17204117.utils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 * @date 2020/8/27 15:07
 * getLogger 必须为非静态，因为需要及时关闭 fileHandle
 * 对于多种日志，可以考虑按日志类型用不同文件存储，不一定全部往一个文件里面塞
 * 当然，对于需写入同一个文件的情况可以考虑声明全部变量，然后进行传参，
 * 使用同一个 JerryLogger 对象进行日志的写入
 */
public class JerryLoggerTest {
    private final static int THREAD_COUNT = 50;

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50,
                Integer.MAX_VALUE / 100,
                5,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                (ThreadFactory) Thread::new);
        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPoolExecutor.execute(() -> {
                JerryLogger jerryLogger = JerryLogger.getJerryLogger("Test");
                jerryLogger.info("info");
                jerryLogger.warning("warning");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
//            threadPoolExecutor.execute(()->{
//                Logger logger = Logger.getLogger("233");
//                logger.info("info");
//                logger.warning("warning");
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
        }
        threadPoolExecutor.shutdown();
    }
}
