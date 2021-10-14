package com.tingyu.security.mapper;

import com.tingyu.security.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User loadUserByUsername(@Param("username") String username);

}
