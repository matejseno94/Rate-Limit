package com.zemanta.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Random;

class Worker implements Runnable {
    public static final String URL = "http://localhost:8080/?clientId=";
    public static final int WAIT_INTERVAL = 3000;

    int id;
    String url;

    public Worker(int id) {
        this.id = id;
        url = URL+id;
    }

    private void sendGet() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                Random random = new Random();
                int time = random.nextInt(WAIT_INTERVAL) + 1;
                Thread.sleep(time);
                sendGet();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}