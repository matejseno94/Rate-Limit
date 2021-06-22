package testing.my_rate;
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
                System.out.println("Cleint id processing: " + clientId);
                BlockingQueue<Long> blockingQueue = Utils.pollingElapsedElements(cache.get(clientId));
                System.out.println("Emptying queue size:" + clientId + " Blockingqueue: " + blockingQueue.size());

                if (blockingQueue.size() >= Main.max_calls) {
                    return false;
                }

                blockingQueue.put(timestamp);
                cache.put(clientId, blockingQueue);
                System.out.println("Blocking queue: " + blockingQueue);
                System.out.println("Queue size: " + clientId + " blocikingqueue: " + blockingQueue.size());
                return true;
            } else {
                System.out.println("Cleint id creating queue: " + clientId);
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
