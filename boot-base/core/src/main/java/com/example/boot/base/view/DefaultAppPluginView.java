package com.example.boot.base.view;

import com.example.boot.base.common.entity.jpa.AppPlugin;
import com.example.boot.base.common.view.AppPluginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Created by MingZhe on 2021/12/15 14:14:55
 *
 * @author wmz
 */
@Component
public class DefaultAppPluginView implements AppPluginView {

    @Autowired
    private AppPluginRepository appPluginRepository;

    @Override
    public void save(AppPlugin appPlugin) {
        appPluginRepository.save(appPlugin);
    }

    @Override
    public void deleteById(String id) {
        appPluginRepository.deleteById(id);
    }

    @Override
    public AppPlugin findOne(String id) {
        return appPluginRepository.findById(id).orElseThrow(() -> new RuntimeException("未找到"));
    }

    @Override
    public Page<AppPlugin> findAll(String keyword, Pageable pageable) {
        return appPluginRepository.findAll(pageable);
    }
}
