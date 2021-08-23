package org.liuweiwei.dao;

import org.liuweiwei.model.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserDao extends JpaRepository<TbUser, Long>, Serializable {

}
