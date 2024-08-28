package com.zhaozhong.openAPI.mapper;

import com.zhaozhong.openAPI.model.entity.UserKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 22843
* @description 针对表【user_key(用户ak/sk列表)】的数据库操作Mapper
* @createDate 2024-01-25 16:29:48
* @Entity com.zhaozhong.openAPI.model.entity.UserKey
*/
public interface UserKeyMapper extends BaseMapper<UserKey> {
    UserKey getAkSkByUserId(Long userId);
}




