package com.example.boot.base.view;

import com.example.boot.base.common.entity.jpa.AppPlugin;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by MingZhe on 2021/12/15 14:14:56
 *
 * @author wmz
 */
@Repository
public interface AppPluginRepository extends PagingAndSortingRepository<AppPlugin, String> {
    
}
