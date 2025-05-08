package com.river.code;

import java.util.concurrent.*;

public class LeakyBucketRateLimiter implements RateLimiter{

    /**
     * 流速，即每秒漏 rate 个
     */
    private final int rate;

    /**
     * 漏桶容量
     */
    private final int bucketSize;

    /**
     * 漏桶
     */
    private final BlockingQueue<Object> bucket;

     public LeakyBucketRateLimiter(int rate, int bucketSize) {
         this.rate = rate;
         this.bucketSize = bucketSize;
         this.bucket = new ArrayBlockingQueue<>(bucketSize);
         // 启动一个定时任务来负责漏
         new Thread(this :: leaky).start();
     }

    public void leaky() {
         // 按照恒定速度漏水
        while (!Thread.interrupted()) {
            bucket.poll();
            sleep();
        }
    }

    private void sleep() {
         try {
             Thread.sleep(1000 / rate);
         } catch (InterruptedException e) {
             Thread.currentThread().interrupt();
         }
    }

    @Override
    public boolean acquired() {
        return bucket.offer(1);
    }

    public int currentBucketSize() {
        return bucket.size();
    }

    public int remainingBucketSize() {
        return bucket.remainingCapacity();
    }

}
