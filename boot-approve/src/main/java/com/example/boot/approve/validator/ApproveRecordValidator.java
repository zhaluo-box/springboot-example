package com.example.boot.approve.validator;

import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.service.ApproveConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created  on 2022/3/14 17:17:24
 *
 * @author zl
 */
@Service
public class ApproveRecordValidator {

    @Autowired
    private ApproveConfigManager approveConfigManager;

    public ApproveModel verifyApproveModelIsAvailable(String modelName) {

        ApproveModel model = approveConfigManager.findOfficialEditionModel(modelName);
        Assert.notNull(model, "当前审批缺少启用的的审批模板信息， 请联系管理员检查审批模板配置信息！");
        return model;
    }
}
