package com.hitales.goldlable.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by wangxi on 18/9/5.
 */
@Service
@Slf4j
public class TestService {

    @Async
    public  void doCompare(String string) throws InterruptedException {
         Thread.sleep(1000 * 3);
        log.info( Thread.currentThread().getName() + "---->   " + string);

    }
    @Async
    public Future<String> compare2(String msg){
        Future<String> future;
        try {
            Thread.sleep(1000 * 3);
            log.info( Thread.currentThread().getName() + "---->   " + msg);
            future = new AsyncResult<>("success:" + msg);
        } catch (InterruptedException e) {
            future = new AsyncResult<>("error");
        }
        return future;
    }
}
