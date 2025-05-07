package com.river.code;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindowRateLimiter implements RateLimiter {

    /**
     * 窗口时长，单位为毫秒
     */
    private final long windowSize;

    /**
     * 子窗口时间，也可以外部传入，暂定 100ms
     */
    private final long bucketSizeInMillis = 100L;

    /**
     * 限流大小
     */
    private final int limit;

    /**
     * 滑动窗口，bucketId -> 请求数
     */
    private final ConcurrentHashMap<Long, AtomicInteger> window;

    /**
     * 起始的 bucketId，用于清理旧 bucket
     */
    private long bucketStart;

    /**
     * 窗口中 bucket 的数量 = windowSize / bucketSize
     */
    private final int bucketCount;

    /**
     * 当前总数
     */
    private final AtomicInteger total;

    public SlidingWindowRateLimiter(long windowSize, int limit) {
        if (windowSize % bucketSizeInMillis != 0) {
            throw new IllegalArgumentException("窗口时长必须是子窗口大小的整数倍");
        }
        this.windowSize = windowSize;
        this.limit = limit;
        this.window = new ConcurrentHashMap<>();
        this.total = new AtomicInteger();
        this.bucketCount = (int) (windowSize / bucketSizeInMillis);
        this.bucketStart = System.currentTimeMillis() / bucketSizeInMillis;
    }


    private synchronized void refresh(long curBucket) {
        // 双重检查
        if (window.containsKey(curBucket)) {
            return;
        }
        // 清理过期数据:其实就是更新总数+map
        long newBucketStart = curBucket - bucketCount + 1;
        for (long i = bucketStart; i < newBucketStart; i++) {
            AtomicInteger removed = window.remove(i);
            if (removed != null) {
                total.addAndGet(-removed.get());
            }
        }
        bucketStart = newBucketStart;

        //最后加上新区间
        window.putIfAbsent(curBucket, new AtomicInteger());
    }


    @Override
    public boolean acquired() {
        long curBucket = System.currentTimeMillis() / bucketSizeInMillis;
        // 检查当前区间是否已经初始化，如果没有则进行初始化
        if (!window.containsKey(curBucket)) {
            refresh(curBucket);
        }
        // 尝试从滑动窗口获取元素，知道成功或者线程中断；如果是拒绝策略，不用此循环，只需要 cas 尝试一次即可，失败直接退出
        while (!Thread.interrupted()) {
            int curTotal = total.get();
            if (curTotal + 1 > limit) {
                return false;
            }

            //否则通过 cas 更新
            if (total.compareAndSet(curTotal, curTotal + 1)) {
                window.get(curBucket).incrementAndGet();
                return true;
            }
        }
        return false;
    }
}
