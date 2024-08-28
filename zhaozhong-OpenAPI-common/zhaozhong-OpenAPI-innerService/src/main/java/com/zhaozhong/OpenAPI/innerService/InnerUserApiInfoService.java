package com.zhaozhong.OpenAPI.innerService;


import com.zhaozhong.openAPI.model.entity.User;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;

/**
* @author 22843
* @description 针对表【user_api_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-02-16 23:17:15
*/
public interface InnerUserApiInfoService {
    boolean invokeCount(long apiInfoId, long userId);

    UserApiInfo getInvokeUserApiInfo(String accessKey, String path);
}
