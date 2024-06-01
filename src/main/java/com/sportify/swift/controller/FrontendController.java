package com.sportify.swift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward to index.html so that client-side routing can handle the route
        return "forward:/index.html";
    }
}