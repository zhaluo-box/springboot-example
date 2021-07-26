package com.example.dbhikricp.controller;


import com.example.dbhikricp.config.SpringBeanFlow;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beans/")
public class BeanFlowTestController {

    private SpringBeanFlow springBeanFlow;

    public BeanFlowTestController(SpringBeanFlow springBeanFlow) {
        this.springBeanFlow = springBeanFlow;
    }

    @PostMapping
    public ResponseEntity<String> load(){
        springBeanFlow.handle();
        return ResponseEntity.ok("success");
    }
}
