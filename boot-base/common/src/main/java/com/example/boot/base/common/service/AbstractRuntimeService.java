package com.example.boot.base.common.service;

/**
 *
 */
public abstract class AbstractRuntimeService implements Runnable {

    @Override
    public void run() {
        System.out.println(getInfo());
        handleData();
    }

    protected abstract String getInfo();

    protected abstract void handleData();

}
