package com.zl.box.bootmybaits.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Pager {
    private int pageIndex=0;
    private int pageSize=10;
}
