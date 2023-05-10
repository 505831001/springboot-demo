package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.TbUser;
import com.example.service.TbUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author LiuWeiWei
 * @since 2023-05-08
 */
@RestController
public class UserOperationController {

    @Autowired
    private TbUserService tbUserService;

    @GetMapping(value = "/user/select")
    public String select() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(tbUserService.list());
    }

    @GetMapping(value = "/user/delete")
    public String delete(@RequestParam String id) throws JsonProcessingException {
        System.out.println("The number of rows to delete is:" + id);
        return new ObjectMapper().writeValueAsString(id);
    }

    @PostMapping(value = "/user/insert")
    public void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String gender = request.getParameter("gender");
        String hobby = request.getParameter("hobby");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String deptCode = request.getParameter("deptCode");
        System.out.println("姓名：" + username + "，性别：" + gender + "，爱好：" + hobby + "，电话：" + phone + "，邮箱：" + email + "，地址：" + address + "，部门：" + deptCode);

        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            String[] value = map.get(key);
            StringBuilder sb = new StringBuilder();
            for (String s : value) {
                sb = new StringBuilder(sb.append(s) + ",");
            }
            System.out.println("字段：" + key + "，数值：" + sb.toString().substring(0, sb.toString().lastIndexOf(",")));
        }
        response.sendRedirect("../page/user-select.html");
    }

    @GetMapping(value = "/user/selectById")
    public String selectById(@RequestParam String id) throws JsonProcessingException {
        TbUser result = new TbUser();
        for (TbUser tbUser : tbUserService.list()) {
            if (id.equalsIgnoreCase(String.valueOf(tbUser.getId()))) {
                result = tbUser;
            }
        }
        return new ObjectMapper().writeValueAsString(result);
    }

    @GetMapping(value = "/user/selectByPage")
    public String selectByPage(@RequestParam String current, @RequestParam String size) throws JsonProcessingException {
        System.out.println("Limit：" + current + "，Offset：" + size);
        Page<TbUser> tbUserPage = tbUserService.page(new Page<>(Long.valueOf(current), Long.valueOf(size)));
        // 返回当前页数，当前行数，总行数，列表
        long currentPageNumber = tbUserPage.getCurrent();
        System.out.println("当前页数：" + currentPageNumber);
        long rows = tbUserPage.getSize();
        System.out.println("当前行数：" + rows);
        int totalRows = tbUserPage.getRecords().size();
        System.out.println("所有行数：" + totalRows);
        int totalPage = (int) Math.ceil(totalRows / (double) rows);
        System.out.println("所有页数：" + totalPage);
        List<TbUser> list = tbUserPage.getRecords();
        Map<String, Object> map = new HashMap<>(10);
        map.put("currentPageNumber", currentPageNumber);
        map.put("rows", rows);
        map.put("totalPage", totalPage);
        map.put("list", tbUserService.selectByPage(current, size));
        return new ObjectMapper().writeValueAsString(map);
    }

    @PostMapping(value = "/user/update")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> map = request.getParameterMap();
        for (String key : map.keySet()) {
            String[] value = map.get(key);
            StringBuilder sb = new StringBuilder();
            for (String s : value) {
                sb = new StringBuilder(sb.append(s) + ",");
            }
            System.out.println("字段：" + key + "，数值：" + sb.toString().substring(0, sb.toString().lastIndexOf(",")));
        }
        response.sendRedirect("../page/user-select.html");
    }

    @GetMapping(value = "/user/batch/delete")
    public String batchDelete(@RequestParam String ids) throws JsonProcessingException {
        System.out.println("看看进来都有啥：" + ids);
        String[] idList = ids.split(",");
        for (String id : idList) {
            System.out.println("主键ID：" + id);
        }
        return new ObjectMapper().writeValueAsString(1);
    }
}
