package com.example.mywebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * Web MVC Controller serving one page:
 *
 * - http://localhost:8080/time -> Shows a page with the current time
 */
@Controller
public class MyWebsiteController {

    @Value("${time.url}")
    private String timeUrl;

    @Autowired
    private TimeApiClient timeApiClient;

    @RequestMapping("/time")
    public ModelAndView time() {
        ModelAndView mav = new ModelAndView("time");
        mav.addObject("currentTime", getCurrentTime());
        return mav;
    }

    private String getCurrentTime() {
        try {
            return timeApiClient.getTime();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
