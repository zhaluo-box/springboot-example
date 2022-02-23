package com.example.boot.simple.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO  审计日志 属性待完善
 * Created  on 2022/2/23 17:17:13
 *
 * @author zl
 */
@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditLog {

}
