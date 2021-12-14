package com.example.boot.base.view;

import com.example.boot.base.common.entity.jpa.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultUserView implements UserView {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void insert(User user) {
        var insertSql = "insert into user (id,username,age) values (?,?,?)";
        jdbcTemplate.update(insertSql, user.getId(), user.getUsername(), user.getAge());
    }

    @Override
    public List<Map<String, Object>> findAllByUsernameLike(String username) {
        var querySql = "select * from user where username like  '%" + username + "%'";
        return jdbcTemplate.queryForList(querySql);
    }

    @Override
    //    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(User user) {
        userRepository.save(user);
    }
}
