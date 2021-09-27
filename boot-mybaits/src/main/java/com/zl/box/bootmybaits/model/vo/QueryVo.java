package com.zl.box.bootmybaits.model.vo;

import com.zl.box.bootmybaits.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class QueryVo implements Serializable {
    private static final long serialVersionUID = 2146092936870691751L;
    private User user;
}
