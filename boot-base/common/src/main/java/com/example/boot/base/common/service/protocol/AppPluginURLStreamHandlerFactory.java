package com.example.boot.base.common.service.protocol;

import com.example.boot.base.common.constants.Constant;
import com.example.boot.base.common.view.AppPluginView;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 *
 */
@RequiredArgsConstructor
public class AppPluginURLStreamHandlerFactory implements URLStreamHandlerFactory {

    @NonNull
    private AppPluginView appPluginView;

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {

        if (protocol.equals(Constant.PROTOCOL)) {
            return new AppPluginURLStreamHandler(appPluginView);
        }

        throw new RuntimeException("暂不支持的协议" + protocol);
    }
}
