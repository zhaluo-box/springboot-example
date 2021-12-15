package com.example.boot.base.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 */
@Data
@Accessors(chain = true)
public class PluginInfo {

    private String name;

    private String description;

    private String version;
}
