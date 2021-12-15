package com.example.boot.base.bootstrap.protocol;

import com.example.boot.base.common.bootstrap.BootstrapService;
import com.example.boot.base.common.constants.Constant;
import com.example.boot.base.common.service.protocol.AppPluginURLStreamHandlerFactory;
import com.example.boot.base.common.view.AppPluginView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

/**
 *
 */
@Slf4j
@Service
public class ProtocolInitService implements BootstrapService {

    @Autowired
    private AppPluginView appPluginView;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("注册同步程序的协议：[{}]", Constant.PROTOCOL);

        try {
            final Field factoryField = URL.class.getDeclaredField("factory");
            //JAVA16以上版本，启动时，请携带：--add-opens java.base/java.net=ALL-UNNAMED参数
            //否则可能出现：module java.base does not "opens java.net"错误
            factoryField.setAccessible(true);
            final Field lockField = URL.class.getDeclaredField("streamHandlerLock");
            lockField.setAccessible(true);
            synchronized (lockField.get(null)) {
                final URLStreamHandlerFactory originalUrlStreamHandlerFactory = (URLStreamHandlerFactory) factoryField.get(null);
                factoryField.set(null, null); // setURLStreamHandlerFactory 会检查factory 是否为null
                URL.setURLStreamHandlerFactory(protocol -> {
                    if (protocol.equals(Constant.PROTOCOL)) {
                        return new AppPluginURLStreamHandlerFactory(appPluginView).createURLStreamHandler(protocol);
                    } else {
                        return originalUrlStreamHandlerFactory.createURLStreamHandler(protocol);
                    }
                });
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        log.info("同步程序协议：[{}]注册完成。", Constant.PROTOCOL);

    }

    @Override
    public int getOrder() {
        return Order.PROTOCOL.ordinal();
    }
}
