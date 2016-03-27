package com.dianping.pelican.concurrent;

import java.util.concurrent.*;

/**
 * Created by liming_liu on 15/7/2.
 */
public class ThreadPool {

    private ExecutorService executor;

    public ThreadPool() {
        executor = Executors.newCachedThreadPool(new PelicanThreadFactory());
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize) {

        this(corePoolSize, maximumPoolSize, new SynchronousQueue<Runnable>(), 10000);
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, int keepAliveTime) {
        this(corePoolSize, maximumPoolSize, workQueue, keepAliveTime, new ThreadPoolExecutor.AbortPolicy());
    }

    public ThreadPool(int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, int keepAliveTime,
                            RejectedExecutionHandler handler) {
        this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
                new PelicanThreadFactory(), handler);
    }

    public ThreadPool(String poolName) {
        executor = Executors.newCachedThreadPool(new PelicanThreadFactory(poolName));
    }

    public ThreadPool(String poolName, int corePoolSize, int maximumPoolSize) {

        this(poolName, corePoolSize, maximumPoolSize, new SynchronousQueue<Runnable>(), 10000);
    }

    public ThreadPool(String poolName, int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, int keepAliveTime) {
        this(poolName, corePoolSize, maximumPoolSize, workQueue, keepAliveTime, new ThreadPoolExecutor.AbortPolicy());
    }

    public ThreadPool(String poolName, int corePoolSize, int maximumPoolSize, BlockingQueue<Runnable> workQueue, int keepAliveTime,
                            RejectedExecutionHandler handler) {
        this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
                new PelicanThreadFactory(poolName), handler);
    }

    public void execute(Runnable run) {
        executor.execute(run);
    }

    public <T> Future<T> submit(Callable<T> call) {
        return executor.submit(call);
    }

    public Future<?> submit(Runnable run) {
        return executor.submit(run);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public boolean isShutdown() {
        return executor.isShutdown();
    }

    public void shutdown() {
        executor.shutdown();
    }
}
