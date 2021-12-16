package com.example.boot.base.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/source-code-learns/")
public class SourceCodeLearnController {

    @Autowired
    private List<HandlerMapping> handlerMappings;

    @GetMapping
    public void get(HttpServletRequest request) throws Exception {
        for (HandlerMapping handlerMapping : handlerMappings) {
            var handler = handlerMapping.getHandler(request);
            System.out.println(handler);
        }
    }
}
