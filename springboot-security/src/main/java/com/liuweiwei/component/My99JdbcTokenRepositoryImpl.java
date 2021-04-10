package com.liuweiwei.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author Liuweiwei
 * @since 2021-04-06
 */
@Slf4j
public class My99JdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {

}
