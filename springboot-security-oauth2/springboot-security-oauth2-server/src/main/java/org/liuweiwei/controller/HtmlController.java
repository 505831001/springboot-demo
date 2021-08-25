package org.liuweiwei.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Log4j2
public class HtmlController {

    /**
     * TODO->http://localhost:9201/echo 未配置 OAuth2 资源服务器http.requestMatchers().antMatchers("")。因此只被Spring Security拦截登录即可。
     * @return
     */
    @GetMapping(value = "/echo")
    @ResponseBody
    public String echo() {
        log.info("请求Url.echo()");
        return "Hello World";
    }

    /**
     * TODO->http://localhost:9201/admin/echo?access_token=e2b86af3-827a-4a62-aab0-c41c634937be 未经授权：访问此资源需要完全身份验证。
     * @return
     */
    @GetMapping(value = "/admin/echo")
    @ResponseBody
    public String echo2() {
        log.info("请求Url.echo2012()");
        return "Hello World 2012";
    }

    /**
     * TODO->http://localhost:9201/guest/echo?access_token=e2b86af3-827a-4a62-aab0-c41c634937be unauthorized: Full authentication is required to access this resource.
     * @return
     */
    @GetMapping(value = "/guest/echo")
    @ResponseBody
    public String echo4() {
        log.info("请求Url.echo2014()");
        return "Hello World 2014";
    }
}
