package com.example.boot.base.common.event;

import com.example.boot.base.common.serializable.SerializableBean;
import com.example.boot.base.common.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventData extends SerializableBean {

    /**
     * 冗余设计== TODO　待删除，优化
     */
    private MetaData metaData;

    public EventData(MetaData metaData, Object event) {
        this.metaData = metaData;
        this.setEvent(event);
    }

    public EventData(Object event) {
        this.setEvent(event);
    }

    @JsonIgnore
    public void setEvent(Object event) {
        this.targetBean = event;
        this.typeName = event.getClass().getTypeName();
        this.content = JsonUtil.toJSON(event);
    }

    public Object getEvent() {
        return this.targetBean;
    }

}
