package com.example.boot.base.view;

import com.example.boot.base.common.entity.jpa.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by MingZhe on 2021/11/21 09:9:48
 *
 * @author wmz
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
}
