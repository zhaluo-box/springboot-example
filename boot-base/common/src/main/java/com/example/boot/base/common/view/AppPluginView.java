package com.example.boot.base.common.view;

import com.example.boot.base.common.entity.jpa.AppPlugin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 */
public interface AppPluginView {
    void save(AppPlugin appPlugin);

    void deleteById(String id);

    AppPlugin findOne(String id);

    Page<AppPlugin> findAll(String keyword, Pageable pageable);
}
