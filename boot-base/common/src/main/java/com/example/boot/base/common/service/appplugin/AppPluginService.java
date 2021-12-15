package com.example.boot.base.common.service.appplugin;

import com.example.boot.base.common.entity.jpa.AppPlugin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 */
public interface AppPluginService {

    void create(byte[] bytes);

    void update(AppPlugin appPlugin);

    void delete(String id);

    Page<AppPlugin> findAll(String keyword, Pageable pageable);

    void install(String id);

    void uninstall(String id);

    void run(String id);
}
