package com.example.boot.base.common.service.protocol;

import com.example.boot.base.common.view.AppPluginView;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class AppPluginURLStreamHandler extends URLStreamHandler {

    @NonNull
    private AppPluginView appPluginView;

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new AppPluginUrlConnection(appPluginView, u);
    }
}
