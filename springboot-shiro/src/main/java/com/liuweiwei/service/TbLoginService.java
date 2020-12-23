package com.liuweiwei.service;

import com.liuweiwei.model.TbUser;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
public interface TbLoginService {
    /**
     *
     * @param getMapByName
     * @return
     */
    public TbUser getUserByName(String getMapByName);
}
