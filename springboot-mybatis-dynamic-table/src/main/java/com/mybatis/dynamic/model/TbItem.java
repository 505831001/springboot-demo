package com.mybatis.dynamic.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuweiwei
 * @since 2020-08-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TbItem implements Serializable {
    private static final long serialVersionUID = -1950423695788614535L;
    /**主键ID(MyBatis-Plus默认主键策略:ASSIGN_ID使用了雪花算法(19位),ASSIGN_UUID(32位))*/
    /**@TableId(value = "id", type = IdType.ASSIGN_ID)*/
    private Long id;
    private String title;
    private String sellPoint;
    private Long price;
    private Integer num;
    private String barcode;
    private String image;
    private Long cid;
    private Byte status;
    private Date created;
    private Date updated;
    /**乐观锁(版本号)*/
    //@Version
    //private Integer version;
}