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

    // Inject the RestOperations. This will be an instance of org.springframework.security.oauth2.client.OAuth2RestTemplate
    // which is made available as a Bean by our Oauth2LoginConfig.
    @Autowired
    private RestOperations restOperations;

    @RequestMapping("/time")
    public ModelAndView time() {
        ModelAndView mav = new ModelAndView("time");
        mav.addObject("currentTime", getCurrentTime());
        return mav;
    }

    /**
     * Get the time from an external REST API using the injected RestOperations.
     */
    private String getCurrentTime() {
        try {
            return restOperations.getForObject(timeUrl, String.class);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
