package com.ThreadPool;


/**
 * @program: JUC
 * @description: reject policy
 * @author: ysx
 * @create: 2023-04-21 10:58
 */

@FunctionalInterface
public interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}
