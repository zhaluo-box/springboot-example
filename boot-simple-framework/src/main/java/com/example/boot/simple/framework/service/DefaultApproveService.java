package com.example.boot.simple.framework.service;

/**
 * Created  on 2022/2/24 17:17:00
 *
 * @author zl
 */
public class DefaultApproveService implements ApproveService {

    @Override
    public void approve(long approveInstanceId) {
        
    }

    @Override
    public boolean cancel(long approveInstanceId) {
        return false;
    }

    @Override
    public void transfer(long approveInstanceId, long transfer, String message) {

    }

    @Override
    public void reject(long approveInstanceId, long approveNodeId, String message) {

    }
}
