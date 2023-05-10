package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.TbUser;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2023-05-10
 */
public interface TbUserService extends IService<TbUser> {
    /**
     * 分页查询列表
     *
     * @param current
     * @param size
     * @return
     */
    List<TbUser> selectByPage(String current, String size);
}
