package com.zemanta.utils;

import java.util.concurrent.BlockingQueue;

public class Utils {
    public static final int diff = 5000;
    public static final int max_calls = 4;


    public static synchronized BlockingQueue<Long> pollingElapsedElements(BlockingQueue<Long> blockingQueue) {
        long currentTimestamp = System.currentTimeMillis();
        while(!blockingQueue.isEmpty()) {
            Long element = blockingQueue.peek();
            if ((element + diff) < currentTimestamp) {
                blockingQueue.poll();
            } else {
                break;
            }
        }

        return blockingQueue;
    }
}
