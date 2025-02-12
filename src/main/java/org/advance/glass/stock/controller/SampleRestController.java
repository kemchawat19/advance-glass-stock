package org.advance.glass.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class SampleRestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot 3.2.2! test call";
    }
}
