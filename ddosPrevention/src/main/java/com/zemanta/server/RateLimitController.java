package com.zemanta.server;

import com.zemanta.rate_limit.RateLimit;
import com.zemanta.rate_limit.RateLimitSingleton;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class RateLimitController {

    public static final ConcurrentHashMap<Integer, BlockingQueue<Long>> cache = new ConcurrentHashMap<>();


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> rateLimit(@RequestParam(value="clientId") Integer client) {
        RateLimit rateLimit = RateLimitSingleton.getInstance(cache);
        boolean accepted = rateLimit.canPreprocess(client, System.currentTimeMillis());
        if (accepted) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}