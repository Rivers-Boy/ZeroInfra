package com.river.code;

import com.google.common.util.concurrent.RateLimiter;

public class GuavaRateLimiterTest {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(10);
        double acquire = rateLimiter.acquire(); // 返回等待时间
    }
}
