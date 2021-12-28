package com.example.boot.base.web;

import com.example.boot.base.common.entity.jpa.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户测试
 */
@Slf4j
@RestController
@RequestMapping("/tests/")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private HandlerMapping handlerMapping;

    @GetMapping
    public void testListParamForGetRequest(@RequestParam List<User> users) {
        users.forEach(System.out::println);
    }

    @PostMapping
    public Page<User> testPageableBody(@RequestBody User user, Pageable pageable, HttpServletRequest request) throws Exception {

        var handler = handlerMapping.getHandler(request);
        System.out.println("===============================================");
        System.out.println("HANDLE" + handler.getHandler());
        System.out.println("INTERCEPTOR" + handler.getInterceptorList());
        System.out.println("S" + handler.getInterceptors());
        System.out.println("===============================================");

        var users = new ArrayList<User>(10);
        generateUsers(users);
        return new PageImpl<>(users, pageable, 10);
    }

    /**
     * 测试JDBCTemplate NamedParameters
     */
    @GetMapping("actions/test-jdbc-name-parameters/")
    public void testJdbcNamedParameter() {
        final var insertSql = "INSERT INTO  user(id, address, username, age, height) VALUES (:id,:address,:username,:age,:height)";
        var parameters = new HashMap<String, Object>();
        parameters.put("id", UUID.randomUUID().toString());
        parameters.put("address", "成都");
        namedParameterJdbcTemplate.update(insertSql, new CustomMapSqlParameterSource(parameters));
    }

    /**
     * 测试JDBCTemplate NamedParameters
     */
    @GetMapping("actions/test-jdbc-name-parameter-like/")
    public List<Map<String, Object>> testJdbcNamedParameterLike(@RequestParam String name) {
        final var querySql = "select * from user where username like '%' :name '%' ";
        var parameters = new HashMap<String, Object>();
        parameters.put("name", name);
        return namedParameterJdbcTemplate.queryForList(querySql, new CustomMapSqlParameterSource(parameters));
    }

    private void generateUsers(List<User> users) {
        for (int i = 0; i < 10; i++) {
            users.add(new User().setAge(i).setUsername("xxx-" + i));
        }
    }

    class CustomMapSqlParameterSource extends AbstractSqlParameterSource {

        private final Map<String, Object> values = new LinkedHashMap<>();

        public CustomMapSqlParameterSource(Map<String, Object> map) {
            addValues(map);
        }

        /**
         * Add a parameter to this parameter source.
         *
         * @param paramName the name of the parameter
         * @param value     the value of the parameter
         * @return a reference to this parameter source,
         * so it's possible to chain several calls together
         */
        public AbstractSqlParameterSource addValue(String paramName, @Nullable Object value) {
            Assert.notNull(paramName, "Parameter name must not be null");
            this.values.put(paramName, value);
            if (value instanceof SqlParameterValue) {
                registerSqlType(paramName, ((SqlParameterValue) value).getSqlType());
            }
            return this;
        }

        /**
         * Add a parameter to this parameter source.
         *
         * @param paramName the name of the parameter
         * @param value     the value of the parameter
         * @param sqlType   the SQL type of the parameter
         * @return a reference to this parameter source,
         * so it's possible to chain several calls together
         */
        public AbstractSqlParameterSource addValue(String paramName, @Nullable Object value, int sqlType) {
            Assert.notNull(paramName, "Parameter name must not be null");
            this.values.put(paramName, value);
            registerSqlType(paramName, sqlType);
            return this;
        }

        /**
         * Add a parameter to this parameter source.
         *
         * @param paramName the name of the parameter
         * @param value     the value of the parameter
         * @param sqlType   the SQL type of the parameter
         * @param typeName  the type name of the parameter
         * @return a reference to this parameter source,
         * so it's possible to chain several calls together
         */
        public AbstractSqlParameterSource addValue(String paramName, @Nullable Object value, int sqlType, String typeName) {
            Assert.notNull(paramName, "Parameter name must not be null");
            this.values.put(paramName, value);
            registerSqlType(paramName, sqlType);
            registerTypeName(paramName, typeName);
            return this;
        }

        /**
         * Add a Map of parameters to this parameter source.
         *
         * @param values a Map holding existing parameter values (can be {@code null})
         * @return a reference to this parameter source,
         * so it's possible to chain several calls together
         */
        public AbstractSqlParameterSource addValues(@Nullable Map<String, ?> values) {
            if (values != null) {
                values.forEach((key, value) -> {
                    this.values.put(key, value);
                    if (value instanceof SqlParameterValue) {
                        registerSqlType(key, ((SqlParameterValue) value).getSqlType());
                    }
                });
            }
            return this;
        }

        /**
         * Expose the current parameter values as read-only Map.
         */
        public Map<String, Object> getValues() {
            return Collections.unmodifiableMap(this.values);
        }

        @Override
        public boolean hasValue(String paramName) {
            return this.values.containsKey(paramName);
        }

        @Override
        @NonNull
        public String[] getParameterNames() {
            return StringUtils.toStringArray(this.values.keySet());
        }

        @Override
        public Object getValue(String paramName) {
            return values.get(paramName);
        }
    }
}
