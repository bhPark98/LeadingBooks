package com.springboot.leadingbooks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BootStrapController {

    @RequestMapping("/dashboard")
    public String dashboard() {
        return "books/utilities-other";
    }
}
