package com.zhaozhong.openAPI.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaozhong.openAPI.model.dto.apiInfo.ApiInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import com.zhaozhong.openAPI.model.vo.ApiInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 22843
* @description 针对表【api_info】的数据库操作Service
* @createDate 2024-01-04 10:04:12
*/
public interface ApiInfoService extends IService<ApiInfo> {

    void validApiInfo(ApiInfo apiInfo, boolean b);

    QueryWrapper<ApiInfo> getQueryWrapper(ApiInfoQueryRequest postQueryRequest);

    Page<ApiInfoVO> getApiInfoVOPage(Page<ApiInfo> apiInfoPage, HttpServletRequest request);

    ApiInfoVO getApiInfoVO(ApiInfo apiInfo, HttpServletRequest request);

    ApiInfo getApiInfoByPath(String apiUrl);
}
