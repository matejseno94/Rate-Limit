package com.zemanta.rate_limit;

import com.zemanta.utils.Utils;

import java.util.concurrent.*;

public class RateLimit {
    ConcurrentHashMap<Integer, BlockingQueue<Long>> cache;
    ExecutorService executorService;

    public RateLimit(ConcurrentHashMap<Integer, BlockingQueue<Long>> cache) {
        this.cache = cache;
        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.execute(new EmptyCache(cache));
    }

    public synchronized boolean canPreprocess(int clientId, long timestamp) {

        try {
            if (cache.containsKey(clientId)) {
                BlockingQueue<Long> blockingQueue = Utils.pollingElapsedElements(cache.get(clientId));
                if (blockingQueue.size() >= Utils.max_calls) {
                    return false;
                }

                blockingQueue.put(timestamp);
                cache.put(clientId, blockingQueue);
                return true;
            } else {
                BlockingQueue<Long> blockingQueue = new PriorityBlockingQueue<>();
                blockingQueue.put(timestamp);
                cache.put(clientId, blockingQueue);
                return true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Something went wrong");
        }
    }

    public void stopExecutors() {
        this.executorService.shutdown();
    }

}
