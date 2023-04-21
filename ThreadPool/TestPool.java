package com.ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @program: JUC
 * @description: test
 * @author: ysx
 * @create: 2023-04-21 15:32
 */

@Slf4j(topic = "c.Test")
public class TestPool {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(1, 1000, 2, TimeUnit.MILLISECONDS, (queue, task) -> {
            // 获取线程超时怎么办？
            // 1）死等
            //queue.put(task);
            // 2）带超时等待
            //queue.putWithTimeout(task, 500, TimeUnit.MILLISECONDS);
            // 3) 让调用者放弃任务执行
            //log.info("放弃{}", task);
            // 4) 让调用者抛出异常
            //throw new RuntimeException("任务执行失败 " + task);
            // 5) 让调用者自己执行任务
            task.run();
        });

        for (int i = 0; i < 5; i++) {
            int j = i;
            pool.execute(()->{
                log.info("task-" + (j + 1));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
