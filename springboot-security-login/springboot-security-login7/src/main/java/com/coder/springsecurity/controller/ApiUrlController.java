package com.coder.springsecurity.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Controller
@RequestMapping(value = "${api-url}")
@Api(tags = "转发请求控制器")
public class ApiUrlController {
    @RequestMapping(value = "/getPage")
    public ModelAndView getPage(ModelAndView modelAndView,String pageName){
        modelAndView.setViewName(pageName);
        return modelAndView;
    }
}
