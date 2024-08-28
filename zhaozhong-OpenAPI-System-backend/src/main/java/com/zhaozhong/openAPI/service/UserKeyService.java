package com.zhaozhong.openAPI.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhaozhong.openAPI.model.dto.userKey.UserKeyQueryRequest;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.zhaozhong.openAPI.model.entity.UserKey;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 22843
* @description 针对表【user_key(用户ak/sk列表)】的数据库操作Service
* @createDate 2024-01-25 16:29:48
*/
public interface UserKeyService extends IService<UserKey> {


    void validGenerator(String generator);

    String generateAccessKey(String generator,Long userId);

    String generateSecretKey(String generator,Long userId,String accessKey);

    UserKey getSecretKeyFromUserIdAndAccessKey(Long userId,String accessKey);

    QueryWrapper<UserKey> getQueryWrapper(UserKeyQueryRequest userKeyQueryRequest);

    Long getUserIdByKey(String accessKey);
    public String getSecretKeyByAccessKey(String accessKey);

    Boolean enableKey(Long id);

    Boolean disableKey(Long id);
}
