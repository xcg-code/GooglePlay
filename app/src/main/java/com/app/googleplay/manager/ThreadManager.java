package com.app.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单例模式获取线程池对象
 */
public class ThreadManager {
    private static class SingleClassInstance {
        private static ThreadPool mThreadPool = new ThreadPool(5,10,1L);
    }

    public static ThreadPool getThreadPool() {
        return SingleClassInstance.mThreadPool;
    }


    //线程池
    public static class ThreadPool {
        private int corePoolSize;// 核心线程数
        private int maximumPoolSize;// 最大线程数
        private long keepAliveTime;// 休息时间

        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize, int maximumPoolSize,
                           long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable r) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize,
                        maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(),
                        Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                // 参1:核心线程数;参2:最大线程数;参3:线程休眠时间;参4:时间单位;参5:线程队列;参6:生产线程的工厂;参7:线程异常处理策略
            }
            executor.execute(r);
        }
        //取消任务
        public void cancel(Runnable r){
            if(executor!=null){
                //从线程队列中移除对象
                executor.getQueue().remove(r);
            }
        }
    }
}
