package com.zl.box.bootmybaits.mapper;

import com.zl.box.bootmybaits.common.BaseMapper;
import com.zl.box.bootmybaits.common.BaseQuery;
import com.zl.box.bootmybaits.model.Role;
import com.zl.box.bootmybaits.model.User;
import com.zl.box.bootmybaits.model.dto.UserAccountDto;
import com.zl.box.bootmybaits.model.dto.UserRoleDto;
import com.zl.box.bootmybaits.model.vo.QueryVo;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据性别查询用户.
     * @param sex 性别.
     * @return list
     */
    List<User> selectBySex(String sex);

    Integer updateUserByKey(User user);

    int deleteUserByKey(Integer id);

    List<User> selectUserLikeUsername(String params);

    Integer findTotal();

    List<User> selectByUserVo(QueryVo query);

    List<User> selectByUserVo2(BaseQuery query);

    List<UserAccountDto> selectUserAndAccount();

    List<UserRoleDto> findUserLazyLoadingRole();

    List<Role> selectRoles(int id);
}
