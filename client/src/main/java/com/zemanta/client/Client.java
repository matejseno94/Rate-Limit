package com.zemanta.client;

import com.zemanta.client.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {

    public static void main(String[] argv) {
        if (argv.length < 2 || !Utils.isNumeric(argv[0]) || !Utils.isNumeric(argv[1]))
            throw new RuntimeException("Please add arguments client.java number of clients (int), number of workers (int) ");

        int numClients = Integer.parseInt(argv[0]);
        int numThreads = Integer.parseInt(argv[1]);
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads * numClients);
        for (int id = 0; id < numClients; id++) {
            for (int j = 0; j < numThreads; j++) {
                executorService.execute(new Worker(id));
            }
        }

    }
}
