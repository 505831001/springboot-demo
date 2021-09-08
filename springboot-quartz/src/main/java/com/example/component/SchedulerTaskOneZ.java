package com.example.component;

import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Liuweiwei
 * @since 2021-09-02
 */
@Log4j2
public class SchedulerTaskOneZ {

    private int count = 0;

    private void process() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                log.info("第几种定时器怎么了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + (count++));
            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new Timer().schedule(task, 1000, 5000);
            }
        };
        new ScheduledThreadPoolExecutor(5).scheduleWithFixedDelay(runnable, 1, 5, TimeUnit.SECONDS);
    }
}
