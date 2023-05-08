package com.example.controller;

import com.example.model.TbUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2023-05-06
 */
@RestController
public class JavaScriptQueryRequestController {

    private static final String JSON = "JSON";
    private static final String TEXT = "TEXT";

    /**
     * 基于jquery框架发送get请求返回text/string格式数据
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/jQueryGet")
    public String jQueryGet(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("Account:" + username + ", Password:" + password);
        return username + "" + password;
    }

    /**
     * 基于jquery框架发送post请求返回text/string格式数据
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/jQueryPost")
    public String jQueryPost(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("Account:" + username + ", Password:" + password);
        return username + "" + password;
    }

    /**
     * 基于jquery框架发送ajax请求返回text/string格式数据
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/jQueryAjax")
    public String jQueryAjax(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("Account:" + username + ", Password:" + password);
        return username + "" + password;
    }

    /**
     * 基于jquery框架发送ajax请求返回json格式数据
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/jQueryAjaxJson")
    public String jQueryAjaxJson(HttpServletRequest request) throws JsonProcessingException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("Account:" + username + ", Password:" + password);
        TbUser user = new TbUser();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println("User:" + user.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        System.out.println("json:" + json);
        return json;
    }

    /**
     * 校验用户名称
     * 1. 使用response响应数据
     * response.getWriter().write(new ObjectMapper().writeValueAsString("true"));
     * response.getWriter().write(new ObjectMapper().writeValueAsString("false"));
     * 2. 使用@Controller+@ResponseBody返回数据
     * return new ObjectMapper().writeValueAsString("true");
     * return new ObjectMapper().writeValueAsString("false");
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/validateForm")
    public String validateForm(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        String username = request.getParameter("username");
        System.out.println("The username for form verification is:" + username);
        if ("liuweiwei".equalsIgnoreCase(username)) {
            // response.getWriter().write(new ObjectMapper().writeValueAsString("true"));
            return new ObjectMapper().writeValueAsString("true");
        } else {
            // response.getWriter().write(new ObjectMapper().writeValueAsString("false"));
            return new ObjectMapper().writeValueAsString("false");
        }
    }

    /**
     * 模糊搜索
     * 1. 使用response响应数据
     * response.getWriter().write(new ObjectMapper().writeValueAsString(list));
     * 2. 使用@Controller+@ResponseBody返回数据
     * String json = new ObjectMapper().writeValueAsString(list);
     * return json;
     *
     * @param keyword
     * @return json
     * @throws IOException
     */
    @GetMapping(value = "/search")
    public String search(@RequestParam String keyword) throws IOException {
        System.out.println("Fuzzy query fields:" + keyword);
        List<TbUser> list = new ArrayList<>();
        list.add(new TbUser("LWeiWei", "12345678"));
        list.add(new TbUser("Jessica", "12345678"));
        String json = new ObjectMapper().writeValueAsString(list);
        return json;
    }

    /**
     * 模糊搜索
     * 1. 使用response响应数据
     * response.getWriter().write(new ObjectMapper().writeValueAsString(list));
     * 2. 使用@Controller+@ResponseBody返回数据
     * String json = new ObjectMapper().writeValueAsString(list);
     * return json;
     *
     * @param keyword
     * @param response
     * @return json
     * @throws IOException
     */
    @GetMapping(value = "/search")
    public void search(@RequestParam String keyword, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Fuzzy query fields:" + keyword);
        List<TbUser> list = new ArrayList<>();
        list.add(new TbUser("LWeiWei", "12345678"));
        list.add(new TbUser("Jessica", "12345678"));
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(list));
    }

    /**
     * 模糊搜索
     * 1. 使用response响应数据
     * response.getWriter().write(new ObjectMapper().writeValueAsString(list));
     * 2. 使用@Controller+@ResponseBody返回数据
     * String json = new ObjectMapper().writeValueAsString(list);
     * return json;
     *
     * @param keyword
     * @param response
     * @return json
     * @throws IOException
     */
    @GetMapping(value = "/search/cors")
    public void searchCors(@RequestParam String keyword, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TbUser> list = new ArrayList<>();
        list.add(new TbUser("LWeiWei", "12345678"));
        list.add(new TbUser("Jessica", "12345678"));
        response.setContentType("text/html;charset=utf-8");
        // CORS 解决方案
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.getWriter().write(new ObjectMapper().writeValueAsString(list));
    }

    /**
     * 模糊搜索
     * 1. 使用response响应数据
     * response.getWriter().write(new ObjectMapper().writeValueAsString(list));
     * 2. 使用@Controller+@ResponseBody返回数据
     * String json = new ObjectMapper().writeValueAsString(list);
     * return json;
     *
     * @param keyword
     * @param response
     * @return json
     * @throws IOException
     */
    @GetMapping(value = "/search/jsonp")
    public void searchJsonP(@RequestParam String keyword, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<TbUser> list = new ArrayList<>();
        list.add(new TbUser("LWeiWei", "12345678"));
        list.add(new TbUser("Jessica", "12345678"));
        response.setContentType("text/html;charset=utf-8");
        // JSONP 解决方案
        String callback = request.getParameter("callback");
        response.getWriter().write(callback + "(" + new ObjectMapper().writeValueAsString(list) + ")");
    }
}
