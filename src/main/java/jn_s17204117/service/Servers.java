package jn_s17204117.service;

import jn_s17204117.models.ReadProperties;
import jn_s17204117.utils.JerryLogger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author IITII
 */
public class Servers {
    /**
     * https://blog.csdn.net/u011517841/article/details/79810689
     */
    private final ThreadPoolExecutor factory = new ThreadPoolExecutor(50,
            Integer.MAX_VALUE / 100,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),
            (ThreadFactory) Thread::new);
    private final String siteDir = Objects.requireNonNull(getClass().getClassLoader().getResource("conf/sites")).getFile();

    public void init() {
        // 因为站点数量一般不般比较少，而且不会频繁变动，故只预热一个线程
        factory.prestartCoreThread();
    }

    public void start() {
        JerryLogger logger = new JerryLogger();
        File dir = new File(siteDir);
        // 一定要是一个文件夹
        if (!dir.isDirectory()) {
            logger.severe("无效站点文件夹，请手动创建 conf/sites");
        }
        File[] files = dir.listFiles();
        if (files == null) {
            logger.info("空站点文件夹...");
            return;
        }
        // 初始化一些东西
        init();
        // 遍历文件夹, 创建多个
        for (File file : files) {
            //避免遍历一些奇奇怪怪的东西
            if (!file.isFile()) {
                return;
            }
            /*
            无需考虑多个不同请求同时发生的情况，因为端口不同
            也不需要根据 HttpExchange 里面 header 来进行分流
             */
            factory.execute(() -> {
                try {
                    new SingleServer(
                            new ReadProperties(
                                    file.getAbsolutePath()
                            ).getProp()
                    ).start();
                } catch (IOException e) {
                    logger.severe(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            });
        }
        logger.close();
    }
}
