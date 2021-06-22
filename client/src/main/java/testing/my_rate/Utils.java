package testing.my_rate;

import java.util.concurrent.BlockingQueue;

public class Utils {
    
    public static synchronized BlockingQueue<Long> pollingElapsedElements(BlockingQueue<Long> blockingQueue) {
        long currentTimestamp = System.currentTimeMillis();
        while(!blockingQueue.isEmpty()) {
            Long element = blockingQueue.peek();
            if ((element + Main.diff) < currentTimestamp) {
                blockingQueue.poll();
            } else {
                break;
            }
        }
        
        return blockingQueue;
    }
}
