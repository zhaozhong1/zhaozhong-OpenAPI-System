package com.zhaozhong.openAPI.mapper;

import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 22843
* @description 针对表【user_api_info(用户调用接口关系表)】的数据库操作Mapper
* @createDate 2024-02-16 23:17:15
* @Entity com.zhaozhong.openAPI.model.entity.UserApiInfo
*/
public interface UserApiInfoMapper extends BaseMapper<UserApiInfo> {
    List<UserApiInfo> listTopApiInvokeCount();
}




