package com.example.boot.approve.view;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot.approve.entity.runtime.ApproveInstance;
import com.example.boot.approve.mapper.ApproveInstanceMapper;
import org.springframework.stereotype.Repository;

/**
 * 审批实例服务
 * Created  on 2022/3/15 10:10:02
 *
 * @author zl
 */
@Repository
public class ApproveInstanceView extends ServiceImpl<ApproveInstanceMapper, ApproveInstance> {

}
