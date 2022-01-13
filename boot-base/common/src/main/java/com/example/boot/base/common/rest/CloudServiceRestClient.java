package com.example.boot.base.common.rest;

/**
 *
 */
public class CloudServiceRestClient extends RestClient {

    public CloudServiceRestClient(int connectionTimeout, int readTimeout) {
        super(connectionTimeout, readTimeout);
    }

    @Override
    protected String getBaseUrl() {
        return "http://  instance.getHost() + : + instance.getPort()" + "/event";
    }
}
