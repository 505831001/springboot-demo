package com.example.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 触发定时任务有两种方式：第二种是定时执行。
 * @author Liuweiwei
 * @since 2021-09-02
 */
//@EnableScheduling
//@Component
@Log4j2
public class SchedulerTaskOneB {

    private int count = 0;

    /**
     * 设置每10秒执行一次
     */
    @Scheduled(fixedRate = 10000)
    private void process() {
        log.info("第一种定时器酷毙了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + (count++));
    }
}
