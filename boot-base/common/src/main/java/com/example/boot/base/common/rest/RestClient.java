package com.example.boot.base.common.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@Slf4j
@AllArgsConstructor
public abstract class RestClient {

    //    private final ClientHttpRequestInterceptor nestIamFeignRequestInterceptor;

    private final int connectionTimeout;

    private final int readTimeout;

    /**
     * 没有返回值
     */
    public void postEvent(byte[] eventData) {
        var url = getBaseUrl() + "/events/";
        doRequest(eventData, url);
    }

    /**
     * 有返回值
     */
    public byte[] call(byte[] eventData) {
        var url = getBaseUrl() + "/calls/";
        return doRequest(eventData, url);
    }

    protected abstract String getBaseUrl();

    protected byte[] doRequest(byte[] eventData, String url) {

        final var restTemplate = getRestTemplate();
        //        restTemplate.setInterceptors(List.of(nestIamFeignRequestInterceptor));
        try {
            if (log.isDebugEnabled()) {
                log.debug("往URL:{}发送事件:{}", url, new String(eventData));
            }
            var httpEntity = new HttpEntity<>(eventData, getHttpHeaders());
            return restTemplate.postForObject(url, httpEntity, byte[].class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("往URL:{}，发送事件：{}失败。", url, new String(eventData));
            log.error(e.getResponseBodyAsString());
            throw new RuntimeException(e.getResponseBodyAsString());
        }
    }

    protected HttpHeaders getHttpHeaders() {
        return new HttpHeaders();
    }

    protected RestTemplate getRestTemplate() {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectionTimeout);
        requestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(requestFactory);
    }

}
