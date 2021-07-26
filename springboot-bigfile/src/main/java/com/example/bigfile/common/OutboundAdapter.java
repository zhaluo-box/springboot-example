package com.example.bigfile.common;


import com.example.bigfile.entity.FileInfo;

public interface OutboundAdapter {

    void build(FileInfo fileInfo);
}
