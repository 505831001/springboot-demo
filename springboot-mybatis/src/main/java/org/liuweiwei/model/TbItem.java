package org.liuweiwei.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuweiwei
 * @since 2020-08-14
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TbItem implements Serializable {
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
}