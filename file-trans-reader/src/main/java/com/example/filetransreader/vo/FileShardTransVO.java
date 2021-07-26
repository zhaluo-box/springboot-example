package com.example.filetransreader.vo;

import com.example.filetransreader.entity.FileShard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class FileShardTransVO implements Serializable {

    private static final long serialVersionUID = 1166575404304264983L;
    private String fileId;

    private FileShard shard;

    private File shardFile;
}
