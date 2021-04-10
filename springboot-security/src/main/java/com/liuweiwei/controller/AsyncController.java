package com.liuweiwei.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @author Liuweiwei
 * @since 2021-03-23
 */
@RestController
@Slf4j
public class AsyncController {

    @GetMapping(value = "/sync")
    public String sync() throws InterruptedException {
        log.info("同步方法.主线程开始...");
        Thread.sleep(1000);
        log.info("同步方法.主线程返回...");
        return "successfully";
    }

    @GetMapping(value = "/async")
    public Callable<String> async() throws Exception {
        log.info("异步方法.主线程开始...");
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("异步方法.副线程开始...");
                Thread.sleep(1000);
                log.info("异步方法.副线程返回...");
                return "successfully";
            }
        };
        log.info("异步方法.主线程返回...");
        return callable;
    }

    @GetMapping(value = "/defer")
    public DeferredResult<String> defer() throws Exception {
        log.info("异步方法.主线程开始...");

        DeferredResult<String> result = new DeferredResult<>();
        new Thread(() -> {
            log.info("异步方法.副线程开始...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("异步方法.副线程返回...");
        }).start();

        log.info("异步方法.主线程返回...");
        return result;
    }
}
