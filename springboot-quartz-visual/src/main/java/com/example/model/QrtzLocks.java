package com.example.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Liuweiwei
 * @since 2021-09-08
 */
@Data
public class QrtzLocks implements Serializable {
    private String schedName;
    private String lockName;
}
