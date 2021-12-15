package com.example.boot.base.web;

import com.example.boot.base.common.entity.jpa.AppPlugin;
import com.example.boot.base.common.service.appplugin.AppPluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 *
 */

@RestController
@RequestMapping("/app-plugins/")
public class AppPluginController {

    @Autowired
    private AppPluginService appPluginService;

    /**
     * 新增
     */
    @PostMapping
    public void upload(MultipartFile jarFile) throws IOException {
        validateJarFile(jarFile);
        // 检验是否是jar文件
        appPluginService.create(jarFile.getBytes());
    }

    /**
     * 修改
     */
    @PutMapping("{id}")
    public void update(@RequestBody AppPlugin appPlugin, @PathVariable String id) {
        appPluginService.update(appPlugin);
    }

    /**
     * 删除
     */
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        appPluginService.delete(id);
    }

    @PostMapping("{id}/actions/install/")
    public void install(@PathVariable String id) {
        appPluginService.install(id);
    }

    @PostMapping("{id}/actions/un-install/")
    public void uninstall(@PathVariable String id) {
        appPluginService.uninstall(id);
    }

    @PostMapping("{id}/actions/run/")
    public void run(@PathVariable String id) {
        appPluginService.run(id);
    }

    /**
     * 查询所有
     */
    @GetMapping()
    public Page<AppPlugin> findAll(@RequestParam(required = false) String keyword, Pageable pageable) {
        return appPluginService.findAll(keyword, pageable);
    }

    private void validateJarFile(MultipartFile jarFile) throws IOException {
        String message = "上传的文件内容为空。";

        Assert.notNull(jarFile, message);
        Assert.notNull(jarFile.getBytes(), message);
        Assert.isTrue(jarFile.getBytes().length > 0, message);

        var fileSuffix = Objects.requireNonNull(jarFile.getOriginalFilename()).substring(jarFile.getOriginalFilename().lastIndexOf(".") + 1);
        Assert.isTrue("jar".equals(fileSuffix), "目前插件只支持jar文件, 当前文件后缀为 : " + fileSuffix);

    }

}
