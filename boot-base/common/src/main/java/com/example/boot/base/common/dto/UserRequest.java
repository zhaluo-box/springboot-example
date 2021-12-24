package com.example.boot.base.common.dto;

import com.example.boot.base.common.entity.jpa.User;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;

/**
 *
 */
@Data
@Accessors(chain = true)
@ToString
public class UserRequest {

    private User user;

    private PageRequest pageable;

}
