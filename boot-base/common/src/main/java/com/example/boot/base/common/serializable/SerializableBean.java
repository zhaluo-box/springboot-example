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
     * targetBean.getClass().getTypeName();
     */
    protected String typeName;

    /**
     * json 后的数据 与targetBean 内容一样，只不过一个是对象，一个是json
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
