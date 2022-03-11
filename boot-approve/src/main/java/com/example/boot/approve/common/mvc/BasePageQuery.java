package com.example.boot.approve.common.mvc;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.boot.approve.common.exception.BadRequestException;
import lombok.Data;

import java.util.*;

/**
 * 基础查询VO， 实现简单分页数据的封装
 * VO  查询类型
 * DTO  page分页返回类型
 * Created  on 2022/3/11 10:10:23
 *
 * @author zl
 */
@Data
public class BasePageQuery<VO, DTO> {

    /**
     * 分页默认从第一页开始
     */
    private int pageNum = 1;

    /**
     * 默认分页大小20 但是最好有前端传递, 这里的分页大小只是做一个冗余的健壮性处理
     */
    private int pageSize = 20;

    /**
     * 分页顺序
     */
    private Set<String> orderBy = new LinkedHashSet<>();

    /**
     * 对于GET请求查询，默认 VO 类型采用String
     * 对于很复杂的POST请求查询， VO 类型采用自定已的查询VO
     */
    private VO query;

    public Page<DTO> buildPage() {
        Page<DTO> page = Page.of(pageNum, pageSize);
        if (orderBy.size() > 0) {
            page.addOrder(convertOrder());
        }
        return page;
    }

    /**
     * 将前端传递的排序字符转为Mybatis排序字段
     */
    private List<OrderItem> convertOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        if (orderBy.size() == 0) {
            return orderItems;
        }

        orderBy.stream().map(order -> order.split(",")).peek(orderChar -> {
            if (orderChar.length <= 1) {
                throw new BadRequestException("排序字段异常" + Arrays.toString(orderChar));
            }
        }).forEach(orderChar -> {
            var orderItem = new OrderItem();
            orderItem.setColumn(orderChar[0]);
            orderItem.setAsc("ASC".equalsIgnoreCase(orderChar[1]));
            orderItems.add(orderItem);
        });

        return orderItems;
    }

}
