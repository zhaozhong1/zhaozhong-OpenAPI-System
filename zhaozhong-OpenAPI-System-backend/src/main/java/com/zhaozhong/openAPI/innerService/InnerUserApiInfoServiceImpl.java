package com.zhaozhong.openAPI.innerService;


import com.zhaozhong.OpenAPI.innerService.InnerUserApiInfoService;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.zhaozhong.openAPI.service.UserApiInfoService;
import javassist.runtime.Inner;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
* @author 22843
* @description 针对表【user_api_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-02-16 23:17:15
*/
@Component
@DubboService
public class InnerUserApiInfoServiceImpl implements InnerUserApiInfoService {

    @Resource
    UserApiInfoService userApiInfoService;

    @Override
    public boolean invokeCount(long apiInfoId, long userId) {
        return userApiInfoService.invokeCount(apiInfoId,userId);
    }

    @Override
    public UserApiInfo getInvokeUserApiInfo(String accessKey, String path) {
        return userApiInfoService.getInvokeUserApiInfo(accessKey,path);
    }

}
