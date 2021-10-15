package com.zl.box.bootmybaits.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseQuery<QueryEntity> implements Serializable {
    private static final long serialVersionUID = -5491791402590772889L;
    private QueryEntity entity;
    private Pager pager;
}
