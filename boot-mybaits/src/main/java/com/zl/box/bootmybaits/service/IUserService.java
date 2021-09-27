package com.zl.box.bootmybaits.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.zl.box.bootmybaits.common.BaseQuery;
import com.zl.box.bootmybaits.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {

    User getUser(String id);

    List<User> findAll();

    String save(List<User> users);

    PageInfo<User> findUserBySex(BaseQuery<String> params);

    PageInfo<User> selectUserLikeUsername(BaseQuery<String> username);

    /**
     * poi 导出通过模板导出Excel
     */
    void exportExcelByTemplate(HttpServletRequest request, HttpServletResponse response);
}
