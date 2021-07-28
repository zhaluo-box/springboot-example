package com.example.dbhikricp.controller;


import com.example.dbhikricp.config.SpringBeanFlow;
import com.example.dbhikricp.dto.ResponseDTO;
import com.example.dbhikricp.vo.BaseQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/beans/")
public class BeanFlowTestController {

    private SpringBeanFlow springBeanFlow;

    public BeanFlowTestController(SpringBeanFlow springBeanFlow) {
        this.springBeanFlow = springBeanFlow;
    }

    @PostMapping
    public ResponseEntity<String> load() {
        springBeanFlow.handle();
        return ResponseEntity.ok("success");
    }

    @PostMapping("/post/query")
    public ResponseEntity<ResponseDTO> post(BaseQueryVO query) {
        log.info("--------- : {}", query.toString());
        return ResponseEntity.ok(new ResponseDTO());
    }


    @GetMapping
    public ResponseEntity<BaseQueryVO> get(String path) {
        return ResponseEntity.ok(new BaseQueryVO().setName("axy").setUri("/opt/data/test/aa.csv"));
    }

    @PostMapping("post/path")
    public ResponseEntity<BaseQueryVO> post(String path) {
        return ResponseEntity.ok(new BaseQueryVO().setName("axy").setUri("/opt/data/test/aa.csv"));
    }
}
