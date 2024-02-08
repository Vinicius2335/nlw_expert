package com.github.vinicius2335.certification.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class FirstController {

    @GetMapping
    public String hello() {
        return "Olá Mundo";
    }
}
