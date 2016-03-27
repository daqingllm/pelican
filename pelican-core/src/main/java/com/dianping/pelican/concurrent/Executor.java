package com.dianping.pelican.concurrent;

import com.dianping.pelican.config.Configuration;
import org.apache.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liming_liu on 15/7/2.
 */
public class Executor {

    private ThreadPool threadPool;

    private Logger logger = Logger.getLogger(Executor.class);

    private final static Executor executor = new Executor();

    public static ThreadPool getInstance() {
        return executor.threadPool;
    }

    private Executor() {
        init();
    }

    private void init() {
        int corePoolSize= Configuration.get("concurrent.threadPool.corePoolSize", 50, Integer.class);
        int maximumPoolSize= Configuration.get("concurrent.threadPool.maximumPoolSize",100,Integer.class);
        int keepAliveTime= Configuration.get("concurrent.threadPool.keepAliveTime",2000,Integer.class);
        int blockingQueueCapacity= Configuration.get("concurrent.threadPool.blockingQueueCapacity",100,Integer.class);

        if (threadPool == null) {
            threadPool = new ThreadPool(corePoolSize, maximumPoolSize, new LinkedBlockingQueue<Runnable>(blockingQueueCapacity), keepAliveTime);

            logger.info("executorService has been initialized");

            // adds a shutdown hook to shut down the executorService
            Thread shutdownHook = new Thread() {
                @Override
                public void run() {
                    synchronized (this) {
                        if (!threadPool.isShutdown()) {
                            threadPool.shutdown();
                            logger.info("excecutorService has been shut down");
                        }
                    }
                }
            };
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            logger.info("successfully add shutdown hook");
        }
    }
}
