package main.java;

import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowRateLimiter implements RateLimiter{

    /**
     * 窗口时长，毫秒级
     */
    private final long windowSize;

    /**
     * 限流大小
     */
    private int limit;

    /**
     * 窗口开始时间，毫秒级
     */
    private long windowStartTime;

    /**
     * 计数器
     */
    private AtomicInteger counter = new AtomicInteger();

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
     * 重置窗口
     */
    private void reset() {
        // 起始时间和计数器清零
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
