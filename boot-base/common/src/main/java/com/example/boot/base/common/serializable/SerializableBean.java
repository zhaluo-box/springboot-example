package com.example.boot.base.common.serializable;

import com.example.boot.base.common.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 *
 */
@Data
@NoArgsConstructor
public class SerializableBean {

    @JsonIgnore
    protected Object targetBean;

    /**
     * TODO　注释待补充
     */
    protected String typeName;

    /**
     * TODO  注释待补充
     */
    protected String content;

    public SerializableBean(Object targetBean) {
        this.targetBean = targetBean;
        this.typeName = targetBean.getClass().getTypeName();
        this.content = JsonUtil.toJSON(targetBean);
    }

    @SneakyThrows
    public void setContent(String content) {
        this.content = content;
        this.targetBean = JsonUtil.toObject(content, Class.forName(typeName));
    }

}
