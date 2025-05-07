package com.river.code;

class FixedWindowRateLimiterTest {

    public static void main(String[] args) {


        FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(1000, 2);

        System.out.println(logInfo(1, rateLimiter.acquired()));
        System.out.println(logInfo(2, rateLimiter.acquired()));
        System.out.println(logInfo(3, rateLimiter.acquired())); // 应该失败

        //Thread.sleep(1100); // 等待窗口重置

        System.out.println("等待窗口重置后...");
        System.out.println(logInfo(4, rateLimiter.acquired()));
        System.out.println(logInfo(5, rateLimiter.acquired()));
        System.out.println(logInfo(6, rateLimiter.acquired())); // 应该失败

    }

    public static String logInfo(Integer i, boolean flag) {
        return "第%d次请求: %s".formatted(i, flag ? "获取成功" : "获取失败");
    }

}