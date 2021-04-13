package org.liuweiwei.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Controller
public class TbUserController {

    @GetMapping(value = "/user")
    public Authentication user(Authentication user) {
        return user;
    }
}
