package org.liuweiwei.controller;

import org.liuweiwei.model.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Controller
public class TbUserController {

    @Autowired
    private TbUserService userService;

    @PostMapping(value = "/insert")
    public String insert() {
        return "insert_page";
    }

    @DeleteMapping(value = "/delete")
    public String delete() {
        return "delete_page";
    }

    @PutMapping(value = "/update")
    public String update() {
        return "update_page";
    }

    @GetMapping(value = "/findAll")
    @ResponseBody
    public Object findAll() {
        List<TbUser> list = userService.findAll();
        return list;
    }

    @GetMapping(value = "/findByUserId")
    @ResponseBody
    public Object findByUserId(@RequestParam(name = "", required = false, defaultValue = "1") Long userId) {
        TbUser user = userService.findByUserId(userId);
        return user;
    }
}