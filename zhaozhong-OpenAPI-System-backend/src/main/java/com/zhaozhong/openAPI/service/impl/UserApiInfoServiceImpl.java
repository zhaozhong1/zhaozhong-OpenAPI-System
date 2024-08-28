package com.zhaozhong.openAPI.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhaozhong.OpenAPI.innerService.InnerUserApiInfoService;
import com.zhaozhong.openAPI.common.ErrorCode;
import com.zhaozhong.openAPI.constant.DBConstant;
import com.zhaozhong.openAPI.exception.BusinessException;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.model.dto.userApiInfo.UserApiInfoQueryRequest;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.zhaozhong.openAPI.service.ApiInfoService;
import com.zhaozhong.openAPI.service.UserApiInfoService;
import com.zhaozhong.openAPI.mapper.UserApiInfoMapper;
import com.zhaozhong.openAPI.service.UserKeyService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 22843
* @description 针对表【user_api_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2024-02-16 23:17:15
*/
@Service
public class UserApiInfoServiceImpl extends ServiceImpl<UserApiInfoMapper, UserApiInfo>
    implements UserApiInfoService, InnerUserApiInfoService {

    @Resource
    ApiInfoService apiInfoService;

    @Resource
    UserKeyService userKeyService;

    @Override
    @DS(DBConstant.USER_CENTER)
    public void validUserApiInfo(UserApiInfo userApiInfo, boolean add) {
        if (userApiInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userApiInfo.getUserId();
        Long apiInfoId = userApiInfo.getApiInfoId();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(userId < 0 || apiInfoId < 0, ErrorCode.PARAMS_ERROR);
        }

        UserApiInfoQueryRequest userApiInfoQueryRequest = new UserApiInfoQueryRequest();
        userApiInfoQueryRequest.setUserId(userId);
        userApiInfoQueryRequest.setApiInfoId(apiInfoId);
        QueryWrapper<UserApiInfo> userApiInfoQueryWrapper = this.getQueryWrapper(userApiInfoQueryRequest);
        UserApiInfo result = this.getOne(userApiInfoQueryWrapper);
            // 如果查询结果不为空，说明数据库存在该记录，因此不可添加新记录，需要抛出异常
        ThrowUtils.throwIf(result != null,ErrorCode.PARAMS_ERROR,"该记录已存在，请勿重复添加！");



        Integer totalNum = userApiInfo.getTotalNum();
        // 校验totalNum长度是否符合规则
        if (totalNum < 10000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总调用次数需不少于10000次");
        }

    }

    /**
     * 获取查询包装类
     *
     * @param userApiInfoQueryRequest
     * @return
     */
    @Override
    @DS(DBConstant.USER_CENTER)
    public QueryWrapper<UserApiInfo> getQueryWrapper(UserApiInfoQueryRequest userApiInfoQueryRequest) {
        QueryWrapper<UserApiInfo> queryWrapper = new QueryWrapper<>();
        if (userApiInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = userApiInfoQueryRequest.getId();
        Long userId = userApiInfoQueryRequest.getUserId();
        Long apiInfoId = userApiInfoQueryRequest.getApiInfoId();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"user_id",userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(apiInfoId),"api_info_id",apiInfoId);
        return queryWrapper;
    }

    @Override
    public Page<UserApiInfo> getUserApiInfoVOPage(Page<UserApiInfo> apiInfoPage, HttpServletRequest request) {
        return null;
    }
    @Override
    @DS(DBConstant.USER_CENTER)
    public boolean invokeCount(long apiInfoId, long userId){
        ThrowUtils.throwIf(userId < 0 || apiInfoId < 0, ErrorCode.PARAMS_ERROR);
        UpdateWrapper<UserApiInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("api_info_id",apiInfoId);
        updateWrapper.eq("user_id",userId);
        updateWrapper.setSql("total_num = total_num + 1,left_num = left_num - 1");
        return this.update(updateWrapper);
    }

    /**
     * 请求调用用户的UserApiInfo（供网关远程调用）
     * @param accessKey
     * @param path
     * @return
     */
    @Override
    public UserApiInfo getInvokeUserApiInfo(String accessKey, String path) {

        ThrowUtils.throwIf(StringUtils.isAnyBlank(accessKey,path),ErrorCode.PARAMS_ERROR);
        DynamicDataSourceContextHolder.push(DBConstant.API_PLATFORM);
        ApiInfo apiInfo = apiInfoService.getApiInfoByPath(path);
        DynamicDataSourceContextHolder.poll();
        Long apiInfoId = apiInfo.getId();
        DynamicDataSourceContextHolder.push(DBConstant.USER_CENTER);
        Long userId = userKeyService.getUserIdByKey(accessKey);

        UserApiInfoQueryRequest userApiInfoQueryRequest = new UserApiInfoQueryRequest();
        userApiInfoQueryRequest.setApiInfoId(apiInfoId);
        userApiInfoQueryRequest.setUserId(userId);
        QueryWrapper<UserApiInfo> userApiInfoQueryWrapper = getQueryWrapper(userApiInfoQueryRequest);
        UserApiInfo userApiInfo = this.getOne(userApiInfoQueryWrapper);
        ThrowUtils.throwIf(userApiInfo == null,ErrorCode.NOT_FOUND_ERROR);
        DynamicDataSourceContextHolder.poll();
        return userApiInfo;
    }


}




