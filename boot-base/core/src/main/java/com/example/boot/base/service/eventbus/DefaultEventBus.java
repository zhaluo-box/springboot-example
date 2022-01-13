package com.example.boot.base.service.eventbus;

import com.example.boot.base.common.event.EventBus;
import com.example.boot.base.common.event.EventData;
import com.example.boot.base.common.event.MetaData;
import com.example.boot.base.common.service.event.EventDispatchService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * 门面与装饰者的结合
 * 默认的事件总线
 */
@Service
public class DefaultEventBus implements EventBus, InitializingBean {

    private SyncMemoryEventBus syncMemoryEventBus;

    private RestEventBus restEventBus;

    @Autowired
    private EventDispatchService eventDispatchService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public void send(Object event) {
        var eventData = wrapEventData(event);
        getTxTemplate().executeWithoutResult(ac -> syncMemoryEventBus.send(eventData));//先确保本系统正常处理全部的事件,且一定会在事务提交以后再将数据发送给运行时态
        restEventBus.send(eventData); //rest 实现
    }

    @Override
    public <T> List<EventSendResult<T>> sendAndWait(Object event, Class<T> expectedClazz) {
        var eventData = wrapEventData(event);
        getTxTemplate().executeWithoutResult(ac -> syncMemoryEventBus.send(eventData));
        return restEventBus.sendAndWait(eventData, expectedClazz);
    }

    @Override
    public <T> T sendAndWait(String targetInstance, Object event, Class<T> expectedClazz) {
        var eventData = wrapEventData(event);
        getTxTemplate().executeWithoutResult(ac -> syncMemoryEventBus.send(eventData));
        return restEventBus.sendAndWait(targetInstance, eventData, expectedClazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        syncMemoryEventBus = new SyncMemoryEventBus(eventDispatchService);
        restEventBus = new RestEventBus();
    }

    private EventData wrapEventData(Object event) {
        var eventData = new EventData();
        eventData.setEvent(event);
        eventData.setMetaData(new MetaData());
        return eventData;
    }

    private TransactionOperations getTxTemplate() {
        var txTemplate = new TransactionTemplate(transactionTemplate.getTransactionManager());
        txTemplate.setPropagationBehaviorName("PROPAGATION_REQUIRES_NEW");
        return txTemplate;
    }
}
