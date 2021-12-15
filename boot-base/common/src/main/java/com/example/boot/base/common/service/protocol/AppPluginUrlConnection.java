package com.example.boot.base.common.service.protocol;

import com.example.boot.base.common.view.AppPluginView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 */
public class AppPluginUrlConnection extends URLConnection {

    private String appPluginId;

    private AppPluginView appPluginView;

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected AppPluginUrlConnection(AppPluginView appPluginView, URL url) {
        super(url);
        this.appPluginView = appPluginView;
        this.appPluginId = url.getPath();
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(appPluginView.findOne(appPluginId).getJarContent());
    }
}
