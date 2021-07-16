package org.liuweiwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.liuweiwei.model.TbUser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService extends IService<TbUser> {

    /**
     * 根据用户ID查询用户信息
     *
     * @param tbUser
     * @return
     */
    TbUser getOne(TbUser tbUser);

    /**
     * 查询用户列表
     *
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    List<TbUser> getAll() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
