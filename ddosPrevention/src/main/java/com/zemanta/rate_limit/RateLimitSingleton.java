package com.zemanta.rate_limit;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitSingleton {
    private static volatile RateLimit instance;
    private static final Object mutex = new Object();

    public static RateLimit getInstance(ConcurrentHashMap<Integer, BlockingQueue<Long>> cache) {
        RateLimit result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new RateLimit(cache);
            }
        }
        return result;
    }
}
