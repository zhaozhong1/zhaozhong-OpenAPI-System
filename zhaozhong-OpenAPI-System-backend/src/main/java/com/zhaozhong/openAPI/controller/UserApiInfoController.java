package com.zhaozhong.openAPI.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaozhong.openAPI.annotation.AuthCheck;
import com.zhaozhong.openAPI.common.*;
import com.zhaozhong.openAPI.constant.UserConstant;
import com.zhaozhong.openAPI.exception.BusinessException;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.model.dto.userApiInfo.UserApiInfoAddRequest;
import com.zhaozhong.openAPI.model.dto.userApiInfo.UserApiInfoQueryRequest;
import com.zhaozhong.openAPI.model.dto.userApiInfo.UserApiInfoUpdateRequest;
import com.zhaozhong.openAPI.model.entity.User;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.zhaozhong.openAPI.service.UserApiInfoService;
import com.zhaozhong.openAPI.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userApiInfo")
@Slf4j
public class UserApiInfoController {
    @Resource
    private UserApiInfoService userApiInfoService;

    @Resource
    private UserService userService;

    /**
     * 创建
     *
     * @param userApiInfoAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserApiInfo(@RequestBody UserApiInfoAddRequest userApiInfoAddRequest) {
        if (userApiInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserApiInfo userApiInfo = new UserApiInfo();
        BeanUtils.copyProperties(userApiInfoAddRequest, userApiInfo);

        userApiInfoService.validUserApiInfo(userApiInfo, true);
        boolean result = userApiInfoService.save(userApiInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newApiInfoId = userApiInfo.getId();
        return ResultUtils.success(newApiInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteApiInfo(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        UserApiInfo oldUserApiInfo = userApiInfoService.getById(id);
        ThrowUtils.throwIf(oldUserApiInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        boolean b = userApiInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param userApiInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateApiInfo(@RequestBody UserApiInfoUpdateRequest userApiInfoUpdateRequest) {
        if (userApiInfoUpdateRequest == null || userApiInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserApiInfo userApiInfo = new UserApiInfo();
        BeanUtils.copyProperties(userApiInfoUpdateRequest, userApiInfo);

        // 参数校验
        userApiInfoService.validUserApiInfo(userApiInfo, true);
        long id = userApiInfoUpdateRequest.getId();
        // 判断是否存在
        UserApiInfo oldUserApiInfo = userApiInfoService.getById(id);
        ThrowUtils.throwIf(oldUserApiInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = userApiInfoService.updateById(userApiInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserApiInfo> getApiInfoVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserApiInfo userApiInfo = userApiInfoService.getById(id);
        if (userApiInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(userApiInfo);
    }

    /**
     * 分页获取列表
     *
     * @param userApiInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserApiInfo>> listApiInfoByPage(@RequestBody UserApiInfoQueryRequest userApiInfoQueryRequest) {
        long current = userApiInfoQueryRequest.getCurrent();
        long size = userApiInfoQueryRequest.getPageSize();
        Page<UserApiInfo> apiInfoPage = userApiInfoService.page(new Page<>(current, size),
                userApiInfoService.getQueryWrapper(userApiInfoQueryRequest));
        return ResultUtils.success(apiInfoPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param userApiInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserApiInfo>> listApiInfoVOByPage(@RequestBody UserApiInfoQueryRequest userApiInfoQueryRequest,
                                                             HttpServletRequest request) {
        long current = userApiInfoQueryRequest.getCurrent();
        long size = userApiInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserApiInfo> userApiInfoPage = userApiInfoService.page(new Page<>(current, size),
                userApiInfoService.getQueryWrapper(userApiInfoQueryRequest));
        return ResultUtils.success(userApiInfoService.getUserApiInfoVOPage(userApiInfoPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param userApiInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<UserApiInfo>> listMyApiInfoVOByPage(@RequestBody UserApiInfoQueryRequest userApiInfoQueryRequest,
                                                               HttpServletRequest request) {
        if (userApiInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        userApiInfoQueryRequest.setUserId(loginUser.getId());
        long current = userApiInfoQueryRequest.getCurrent();
        long size = userApiInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserApiInfo> apiInfoPage = userApiInfoService.page(new Page<>(current, size),
                userApiInfoService.getQueryWrapper(userApiInfoQueryRequest));
        return ResultUtils.success(userApiInfoService.getUserApiInfoVOPage(apiInfoPage, request));
    }




}
