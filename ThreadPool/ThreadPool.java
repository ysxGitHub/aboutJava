package com.ThreadPool;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @program: JUC
 * @description: thread pool
 * @author: ysx
 * @create: 2023-04-21 10:57
 */
public class ThreadPool {
    private BlockingQueue<Runnable> taskQueue;
    // 线程数
    private int coreSize;

    private HashSet<Worker> workers = new HashSet<>();

    // 获取任务的超时间
    private long timeout;
    private TimeUnit unit;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, int capacity, TimeUnit unit, RejectPolicy<Runnable> rejectPolicy) {
        this.taskQueue = new BlockingQueue<>(capacity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.unit = unit;
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task) {
        synchronized (workers) {
            if (coreSize > workers.size()) {
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start();
            } else {
                // taskQueue.put(task);
                rejectPolicy.reject(taskQueue, task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            while (task != null || (task = taskQueue.takeWithTimeout(timeout, unit)) != null) {
                try {
                    task.run();
                } catch (Exception e) {

                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                workers.remove(this);
            }
        }
    }
}
