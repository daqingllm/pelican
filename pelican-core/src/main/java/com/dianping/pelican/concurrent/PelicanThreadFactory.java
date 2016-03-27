package com.dianping.pelican.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liming_liu on 15/7/2.
 */
public class PelicanThreadFactory implements ThreadFactory {

    static final AtomicInteger poolNumber = new AtomicInteger(1);
    final AtomicInteger threadNumber;
    final ThreadGroup group;
    final String namePrefix;
    final boolean isDaemon;

    public PelicanThreadFactory(){
        this("Pelican-Pool");
    }

    public PelicanThreadFactory(String name) {
        this(name, true);
    }

    public PelicanThreadFactory(String preffix, boolean daemon){
        this.threadNumber = new AtomicInteger(1);

        this.group = new ThreadGroup(preffix + "-" + poolNumber.getAndIncrement() + "-threadGroup");

        this.namePrefix = preffix + "-" + poolNumber.getAndIncrement() + "-thread-";
        this.isDaemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r){
        Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);

        t.setDaemon(this.isDaemon);
        if (t.getPriority() != 5)
            t.setPriority(5);

        return t;
    }

    /**
     * @return the group
     */
    public ThreadGroup getGroup() {
        return group;
    }
}
