package com.example.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;

/**
 * @author Liuweiwei
 * @since 2021-09-08
 */
@Data
public class QrtzTriggers implements Serializable {
    private String schedName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String description;
    private Long nextFireTime;
    private Long prevFireTime;
    private Integer priority;
    private String triggerState;
    private String triggerType;
    private Long startTime;
    private Long endTime;
    private String calendarName;
    private Integer misfireInstr;
    private Blob jobData;
}
