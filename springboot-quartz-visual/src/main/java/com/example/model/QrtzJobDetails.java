package com.example.model;

import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;

/**
 * @author Liuweiwei
 * @since 2021-09-07
 */
@Data
public class QrtzJobDetails implements Serializable {
    private String schedName;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String isDurable;
    private String isNonconcurrent;
    private String isUpdateData;
    private String requestsRecovery;
    private Blob jobData;
}
