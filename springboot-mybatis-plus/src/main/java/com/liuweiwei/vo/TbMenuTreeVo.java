package com.liuweiwei.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.liuweiwei.model.TbMenuTree;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TbMenuTreeVo implements Serializable {
    /**类别ID*/
    private String id;
    /**父类别ID*/
    private String parentId;
    /**菜单名称*/
    private String menuName;
    /**子级列表*/
    private List<TbMenuTreeVo> children;
}
