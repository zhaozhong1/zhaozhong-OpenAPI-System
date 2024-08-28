package com.zhaozhong.openAPI.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaozhong.openAPI.model.dto.apiInfo.ApiInfoQueryRequest;
import com.zhaozhong.openAPI.model.dto.userApiInfo.UserApiInfoQueryRequest;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 22843
* @description 针对表【user_api_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-02-16 23:17:15
*/
public interface UserApiInfoService extends IService<UserApiInfo> {

    void validUserApiInfo(UserApiInfo userApiInfo, boolean add);

    QueryWrapper<UserApiInfo> getQueryWrapper(UserApiInfoQueryRequest userApiInfoQueryRequest);

    Page<UserApiInfo> getUserApiInfoVOPage(Page<UserApiInfo> userApiInfoPage, HttpServletRequest request);

    boolean invokeCount(long apiInfoId, long userId);

    UserApiInfo getInvokeUserApiInfo(String accessKey, String path);
}
