package com.zhaozhong.OpenAPI.innerService;

import com.zhaozhong.openAPI.model.entity.ApiInfo;

import java.util.List;

/**
* @author 22843
* @description 针对表【api_info】的数据库操作Service
* @createDate 2024-01-04 10:04:12
*/
public interface InnerApiInfoService {
    List<ApiInfo> getApiInfoList();
}
