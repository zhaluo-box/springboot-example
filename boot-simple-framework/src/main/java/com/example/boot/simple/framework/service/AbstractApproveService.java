package com.example.boot.simple.framework.service;


/**
 * Created  on 2022/2/24 16:16:49
 *
 * @author zl
 */
public abstract class AbstractApproveService implements ApproveService {

    private ApproveService approveService;

    protected abstract void setApproveService(ApproveService approveService);

    @Override
    public void approve(long approveInstanceId) {

    }

    @Override
    public boolean cancel(long approveInstanceId) {
        return false;
    }

    @Override
    public void transfer(long approveInstanceId, long transfer, String message) {
        transferPreHandle();
        approveService.transfer(approveInstanceId, transfer, message);
        transferPostHandle();
    }

    @Override
    public void reject(long approveInstanceId, long approveNodeId, String message) {
        rejectPreHandle();
        approveService.reject(approveInstanceId, approveNodeId, message);
        rejectPostHandle();
    }

    protected void approvePreHandle() {
        //
    }

    protected void approvePostHandle() {
        //
    }

    protected void cancelPreHandle() {
        //
    }

    protected void cancelPostHandle() {
        //
    }

    protected void transferPreHandle() {
        //
    }

    protected void transferPostHandle() {
        //
    }

    protected void rejectPreHandle() {
        //
    }

    protected void rejectPostHandle() {
        //
    }
}
