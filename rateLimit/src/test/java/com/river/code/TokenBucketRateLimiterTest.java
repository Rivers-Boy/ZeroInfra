package com.river.code;

import static org.junit.jupiter.api.Assertions.*;

class TokenBucketRateLimiterTest {

    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(2, 10);

        for (int i = 0; i < 10; i++) {
            //Thread.sleep(500);
            System.out.println(logInfo(i, tokenBucketRateLimiter.acquired()));
        }
    }

    public static String logInfo(Integer i, boolean flag) {
        return "第%d次请求: %s".formatted(i, flag ? "获取成功" : "获取失败");
    }


}