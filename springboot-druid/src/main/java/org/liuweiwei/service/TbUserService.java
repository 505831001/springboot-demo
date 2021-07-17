package org.liuweiwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.liuweiwei.model.TbUser;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService extends IService<TbUser> {
    /**
     * 根据用户字段查询用户信息
     *
     * @param tbUser
     * @return
     */
    TbUser otherOne(TbUser tbUser);

    /**
     * 查询用户列表
     *
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    List<TbUser> otherAll() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 根据用户主键查询用户详情
     *
     * @param id
     * @return
     */
    TbUser otherDetails(Serializable id);
}
