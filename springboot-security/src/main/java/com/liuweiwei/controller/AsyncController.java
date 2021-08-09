package com.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
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
@Api(value = "", tags = "同步或异步线程控制器")
@Log4j2
public class AsyncController {

    @GetMapping(value = "/sync")
    @ApiOperation(value = "同步方法", notes = "同步方法", tags = "")
    public String sync() throws InterruptedException {
        log.info("同步方法.主线程开始...");
        Thread.sleep(1000);
        log.info("同步方法.主线程返回...");
        return "successfully";
    }

    @GetMapping(value = "/async")
    @ApiOperation(value = "异步方法", notes = "异步方法", tags = "")
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
    @ApiOperation(value = "异步方法二", notes = "异步方法二", tags = "")
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
