package testing.my_rate;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Tester implements Runnable {
    private final Integer miliseconds;
    RateLimit rateLimit;
    Integer clientId;
    public Tester(RateLimit rateLimit, Integer clientId, Integer miliscond) {
        this.rateLimit = rateLimit;
        this.clientId = clientId;
        this.miliseconds = miliscond;

    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
                System.out.println(this.rateLimit.canPreprocess(clientId, System.currentTimeMillis()));
                System.out.println();
                System.out.println();
                try {
                    Random random = new Random();
                    Thread.sleep(miliseconds);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}

public class Main {

    public static final ConcurrentHashMap<Integer, BlockingQueue<Long>> cache = new ConcurrentHashMap<>();
    public static final int diff = 5000;
    public static final int max_calls = 4;

    public static void main(String[] argv) {
            RateLimit rateLimit = RateLimitSingelton.getInstance(cache);
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            executorService.execute(new Tester(rateLimit,0, 700));
            executorService.execute(new Tester(rateLimit,1, 1500));
            executorService.execute(new Tester(rateLimit,2, 300));

            executorService.shutdown();

    }
}
