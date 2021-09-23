package net.mingsoft.mdiy.dao;

import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.mdiy.entity.ConfigEntity;
import org.springframework.stereotype.Component;

/**
 * 自定义配置持久层
 * @author 铭飞科技
 * 创建日期：2020-5-28 15:12:02<br/>
 * 历史修订：<br/>
 */
@Component("mdiyCoConfigDao")
public interface IConfigDao extends IBaseDao<ConfigEntity> {

}
