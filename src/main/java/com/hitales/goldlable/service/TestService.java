package com.hitales.goldlable.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by wangxi on 18/9/5.
 */
@Service
@Slf4j
public class TestService {

    @Async
    public  void doCompare(String string){
        log.info( Thread.currentThread().getName() + "---->   " + string);

    }

}
