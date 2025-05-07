package com.river.code;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter {

    /**
     * 窗口时长，毫秒级
     */
    private final long windowSize;

    /**
     * 限流大小
     */
    private final int limit;

    /**
     * 窗口开始时间，毫秒级
     */
    private long windowStartTime;

    /**
     * 计数器
     */
    private final AtomicInteger counter = new AtomicInteger();

    public FixedWindowRateLimiter(long windowSize, int limit) {
        this.windowSize = windowSize;
        this.limit = limit;
        reset();
    }

    /**
     * 检查窗口是否有效
     */
    private boolean checkWindowIfValid() {
        return System.currentTimeMillis() - windowStartTime <= windowSize;
    }

    /**
     * 重置窗口:保证只有一个线程在重置它
     * 同时进行一个二重检查：因为假设在重置期间来了 n 个请求，第一个执行成功之后，后面如果不检查，会重置 n 次
     */
    private synchronized void reset() {
        if (checkWindowIfValid()) {
            return;
        }
        // 起始时间和计数器清零
        System.out.println("窗口重置");
        windowStartTime = System.currentTimeMillis();
        counter.set(0);
    }


    @Override
    public boolean acquired() {
        // 首先检查窗口是否有效:无效重置
        if (!checkWindowIfValid()) {
            reset();
        }
        return counter.incrementAndGet() <= limit;
    }
}
