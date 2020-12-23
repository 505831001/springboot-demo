package com.liuweiwei.config;

import com.liuweiwei.component.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Configuration
public class WebMvcShiroConfig {

    /**
     * 将自己的验证方式加入容器。
     * IoC 注入方式之一：@Bean注解。或者使用：@Component注解在它的类上。
     *
     * @return
     * @ConditionalOnMissingBean 注册相同类型Bean就不会成功。
     * @Primary 注册多次，这时需要用来确定你要哪个实现。
     */
    @Bean
    @ConditionalOnMissingBean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    /**
     * 权限管理，配置主要是Realm管理认证
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    /**
     * Filter 工厂，设置对应的过滤条件和跳转条件。
     *
     * @param securityManager
     * @return
     */
    @Bean
    @Primary
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("/logout", "logout");
        map.put("/swagger**/**", "anon");
        map.put("/webjars/**", "anon");
        map.put("/v2/**", "anon");
        map.put("/**", "authc");

        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(securityManager);
        factory.setLoginUrl("/login");
        factory.setSuccessUrl("/index");
        factory.setUnauthorizedUrl("/error");
        factory.setFilterChainDefinitionMap(map);
        return factory;
    }

    /**
     * 加入注解的使用，不加入这个注解不生效。
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}