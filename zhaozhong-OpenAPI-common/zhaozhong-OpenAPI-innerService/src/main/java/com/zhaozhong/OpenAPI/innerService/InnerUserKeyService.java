package com.zhaozhong.OpenAPI.innerService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhaozhong.openAPI.model.dto.userKey.UserKeyQueryRequest;
import com.zhaozhong.openAPI.model.entity.UserKey;

/**
* @author 22843
* @description 针对表【user_key(用户ak/sk列表)】的数据库操作Service
* @createDate 2024-01-25 16:29:48
*/
public interface InnerUserKeyService {
    String getSecretKeyByAccessKey(String accessKey);
}
