package com.example.myrestapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author hvbeneden
 * @since 1/18/18
 */
@RestController
public class TimeController {
    @RequestMapping(value = "/time")
    public String getTime() {
        return LocalDateTime.now().toString();
    }

}
