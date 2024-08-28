package com.zhaozhong.openAPI.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhaozhong.openAPI.common.BaseResponse;
import com.zhaozhong.openAPI.common.ErrorCode;
import com.zhaozhong.openAPI.common.ResultUtils;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.mapper.UserApiInfoMapper;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.zhaozhong.openAPI.model.vo.ApiInfoVO;
import com.zhaozhong.openAPI.service.ApiInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api
@RequestMapping("/analysis")
public class AnalysisAPIController {

    @Resource
    UserApiInfoMapper userApiInfoMapper;

    @Resource
    ApiInfoService apiInfoService;

    @GetMapping("/top/API/invoke")
//    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<ApiInfoVO>> listTopInvokeApiInfo(){
        // 1. 得到userApiInfo表中的前三个接口id
        List<UserApiInfo> userApiInfoList = userApiInfoMapper.listTopApiInvokeCount();
        // 2. 得到id后，去ApiInfo表中查询该接口id对应的接口信息
        Map<Long, List<UserApiInfo>> userApiInfoMap = userApiInfoList.stream().collect(Collectors.groupingBy(UserApiInfo::getApiInfoId));
        QueryWrapper<ApiInfo> apiInfoQueryWrapper = new QueryWrapper<>();
        apiInfoQueryWrapper.in("id",userApiInfoMap.keySet());
        List<ApiInfo> apiInfoList = apiInfoService.list(apiInfoQueryWrapper);
        ThrowUtils.throwIf(CollectionUtil.isEmpty(apiInfoList), ErrorCode.SYSTEM_ERROR);
        // 3. 将接口信息ApiInfo的list封装成ApiInfoVO的list并返回
        List<ApiInfoVO> apiInfoVOList = apiInfoList.stream().map(apiInfo -> {
            ApiInfoVO apiInfoVO = new ApiInfoVO();
            BeanUtils.copyProperties(apiInfo, apiInfoVO);
            Integer totalNum = userApiInfoMap.get(apiInfo.getId()).get(0).getTotalNum();
            apiInfoVO.setTotalNum(totalNum);
            return apiInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(apiInfoVOList);

    }



}
