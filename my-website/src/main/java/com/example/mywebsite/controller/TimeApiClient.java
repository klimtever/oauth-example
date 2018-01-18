package com.example.mywebsite.controller;//package com.jdriven.example.cloudsecurity.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author hvbeneden
 * @since 12/15/17
 */
@FeignClient(name = "timeApi", url = "http://localhost:8081")
public interface TimeApiClient {
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    String getTime();
}
