package com.example.boot.base.common.rest;

/**
 *
 */
public class DefaultRestClient extends RestClient {

    public DefaultRestClient(int connectionTimeout, int readTimeout) {
        super(connectionTimeout, readTimeout);
    }

    @Override
    protected String getBaseUrl() {
        return "http://localhost:10086";
    }
}
