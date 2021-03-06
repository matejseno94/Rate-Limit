package com.zemanta.rate_limit;

import com.zemanta.utils.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class EmptyCache implements Runnable {
    Map<Integer, BlockingQueue<Long>> queue;

    public EmptyCache(Map<Integer, BlockingQueue<Long>> queue) {
        this.queue = queue;
    }

    public void run() {
        while (true) {
            try {
                synchronized (queue) {
                    for (Integer clientId : queue.keySet()) {
                        BlockingQueue<Long> blockingQueue = queue.get(clientId);
                        queue.put(clientId, Utils.pollingElapsedElements(blockingQueue));
                    }

                    Iterator<Integer> it = queue.keySet().iterator();
                    while (it.hasNext()) {
                        BlockingQueue<Long> blockingQueue = queue.get(it.next());
                        if (blockingQueue.size() == 0)
                            it.remove();
                    }
                }
                Thread.sleep(20000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

}