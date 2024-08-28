package com.zhaozhong.openAPI.innerService;

import com.zhaozhong.OpenAPI.innerService.InnerUserKeyService;
import com.zhaozhong.openAPI.service.UserKeyService;
import com.zhaozhong.openAPI.service.impl.UserKeyServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
* @author 22843
* @description 针对表【api_info】的数据库操作Service
* @createDate 2024-01-04 10:04:12
*/
@Component
@DubboService
public class InnerUserKeyServiceImpl implements InnerUserKeyService {

    @Resource
    UserKeyService userKeyService;


    @Override
    public String getSecretKeyByAccessKey(String accessKey) {
        return userKeyService.getSecretKeyByAccessKey(accessKey);
    }
}
