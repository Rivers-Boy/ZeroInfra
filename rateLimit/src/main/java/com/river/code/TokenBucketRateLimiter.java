package com.river.code;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketRateLimiter implements RateLimiter {

    /**
     * 每秒补充的令牌数
     */
    private final int rate;

    /**
     * 最大令牌数量
     */
    private final int bucketSize;

    /**
     * 令牌桶
     */
    private final AtomicInteger bucket;

    public TokenBucketRateLimiter(int rate, int bucketSize) {
        this.rate = rate;
        this.bucketSize = bucketSize;
        this.bucket = new AtomicInteger(0);
        new Thread(this::refill).start();
    }

    /**
     * 定时补充令牌
     */
    public void refill() {
         while (!Thread.interrupted()) {
             if (bucket.get() < bucketSize) {
                 bucket.incrementAndGet();
             }
             sleep();
         }
    }

    @Override
    public boolean acquired() {
        // 尝试获取令牌
        while (!Thread.interrupted()) {
            int cur = bucket.get();
            if (cur == 0) {
                return false;
            }
            // cas处理
            if (bucket.compareAndSet(cur, cur -1)) {
                return true;
            }
        }
        return false;
    }

    private void sleep() {
        try {
            Thread.sleep(1000 / rate);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
