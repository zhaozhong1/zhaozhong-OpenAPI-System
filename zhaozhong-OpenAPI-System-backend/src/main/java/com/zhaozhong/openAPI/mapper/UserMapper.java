package com.zhaozhong.openAPI.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhaozhong.openAPI.model.entity.User;
import com.zhaozhong.openAPI.test.pojo.UserApiInfoUserUnion;

/**
 * 用户数据库操作
 *

 */
public interface UserMapper extends BaseMapper<User> {

    /**
    * @author 22843
    * @description 针对表【user(用户)】的数据库操作Mapper
    * @createDate 2024-01-25 16:20:29
    * @Entity generator.domain.User
    */

    UserApiInfoUserUnion testLink(Long userId);

}




