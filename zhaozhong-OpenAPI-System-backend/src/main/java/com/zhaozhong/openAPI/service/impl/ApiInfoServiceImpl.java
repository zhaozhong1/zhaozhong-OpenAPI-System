package com.zhaozhong.openAPI.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhaozhong.openAPI.common.ErrorCode;
import com.zhaozhong.openAPI.constant.CommonConstant;
import com.zhaozhong.openAPI.constant.DBConstant;
import com.zhaozhong.openAPI.exception.BusinessException;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.mapper.ApiInfoMapper;
import com.zhaozhong.openAPI.model.dto.apiInfo.ApiInfoQueryRequest;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import com.zhaozhong.openAPI.model.vo.ApiInfoVO;
import com.zhaozhong.openAPI.service.ApiInfoService;
import com.zhaozhong.openAPI.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 22843
* @description 针对表【api_info】的数据库操作Service实现
* @createDate 2024-01-04 10:04:12
*/
@Service
@DS(DBConstant.API_PLATFORM)
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo>
    implements ApiInfoService{

    @Override
    public void validApiInfo(ApiInfo apiInfo, boolean add) {
        if (apiInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String apiName = apiInfo.getApiName();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(apiName), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(apiName) && apiName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "api名字不可过长");
        }

    }
    /**
     * 获取查询包装类
     *
     * @param apiInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<ApiInfo> getQueryWrapper(ApiInfoQueryRequest apiInfoQueryRequest) {
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        if (apiInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = apiInfoQueryRequest.getId();
        String apiName = apiInfoQueryRequest.getApiName();
        String apiDescription = apiInfoQueryRequest.getApiDescription();
        String path = apiInfoQueryRequest.getPath();
        String method = apiInfoQueryRequest.getMethod();
        Long userId = apiInfoQueryRequest.getUserId();
        String reqHeader = apiInfoQueryRequest.getReqHeader();
        String respHeader = apiInfoQueryRequest.getRespHeader();
        String sortField = apiInfoQueryRequest.getSortField();
        String sortOrder = apiInfoQueryRequest.getSortOrder();
        String routeId = apiInfoQueryRequest.getRouteId();
        // 拼接查询条件

        queryWrapper.like(StringUtils.isNotBlank(apiName), "api_name", apiName);
        queryWrapper.like(StringUtils.isNotBlank(apiDescription), "api_description", apiDescription);
        queryWrapper.like(StringUtils.isNotBlank(path), "path", path);
        queryWrapper.like(StringUtils.isNotBlank(method), "method", method);
        queryWrapper.like(StringUtils.isNotBlank(reqHeader), "req_header", reqHeader);
        queryWrapper.like(StringUtils.isNotBlank(respHeader), "resp_header", respHeader);

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(routeId), "route_id", routeId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<ApiInfoVO> getApiInfoVOPage(Page<ApiInfo> apiInfoPage, HttpServletRequest request) {
        List<ApiInfo> apiInfoList = apiInfoPage.getRecords();
        Page<ApiInfoVO> apiInfoVOPage = new Page<>(apiInfoPage.getCurrent(), apiInfoPage.getSize(), apiInfoPage.getTotal());
        return apiInfoVOPage;
    }

    // TODO 传递数据安全化（VO）
    @Override
    public ApiInfoVO getApiInfoVO(ApiInfo apiInfo, HttpServletRequest request) {
        return null;
    }

    @Override
    public ApiInfo getApiInfoByPath(String path){
        ThrowUtils.throwIf(StrUtil.isBlank(path),ErrorCode.PARAMS_ERROR);
        ApiInfoQueryRequest apiInfoQueryRequest = new ApiInfoQueryRequest();
        apiInfoQueryRequest.setPath(path);
        QueryWrapper<ApiInfo> queryWrapper = this.getQueryWrapper(apiInfoQueryRequest);
        ApiInfo apiInfo = this.getOne(queryWrapper);
        ThrowUtils.throwIf(apiInfo == null,ErrorCode.NOT_FOUND_ERROR);
        return apiInfo;
    }

}




