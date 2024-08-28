package com.zhaozhong.openAPI.controller;

import cn.hutool.core.util.StrUtil;
import com.zhaozhong.openAPI.annotation.AuthCheck;
import com.zhaozhong.openAPI.common.BaseResponse;
import com.zhaozhong.openAPI.common.DeleteRequest;
import com.zhaozhong.openAPI.common.ErrorCode;
import com.zhaozhong.openAPI.common.ResultUtils;
import com.zhaozhong.openAPI.constant.UserConstant;
import com.zhaozhong.openAPI.exception.BusinessException;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.model.dto.userKey.GetSecretKeyRequest;
import com.zhaozhong.openAPI.model.dto.userKey.UserKeyAddRequest;
import com.zhaozhong.openAPI.model.entity.User;
import com.zhaozhong.openAPI.model.entity.UserKey;
import com.zhaozhong.openAPI.service.UserKeyService;
import com.zhaozhong.openAPI.service.UserService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/key")
public class UserKeyController {

    @Resource
    private UserKeyService userKeyService;

    @Resource
    private UserService userService;

    /**
     * 创建
     * @param userKeyAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.USER_LOGIN_STATE)
    public BaseResponse<Boolean> addUserKey(@RequestBody UserKeyAddRequest userKeyAddRequest, HttpServletRequest request) {
        if (userKeyAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String generator = userKeyAddRequest.getGenerator();
        userKeyService.validGenerator(generator);
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        String accessKey = userKeyService.generateAccessKey(generator,userId);
        String secretKey = userKeyService.generateSecretKey(generator,userId,accessKey);

        UserKey userKey = new UserKey();
        userKey.setUserId(userId);
        userKey.setAccessKey(accessKey);
        userKey.setSecretKey(secretKey);
        boolean saved = userKeyService.save(userKey);

        return ResultUtils.success(saved);
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
        UserKey oldUserKey = userKeyService.getById(id);
        ThrowUtils.throwIf(oldUserKey == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        boolean b = userKeyService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 用户进行启用Key操作
     * @return
     */
    @PostMapping("/enable")
    public BaseResponse<Boolean> enableKey(@RequestParam Long id){
        Boolean enable = userKeyService.enableKey(id);
        // TODO 需要添加一个错误参数：无需更新
        ThrowUtils.throwIf(!enable,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 用户进行禁用Key操作
     * @return
     */
    @PostMapping("/disable")
    public BaseResponse<Boolean> disableKey(@RequestParam Long id){
        // TODO
        Boolean disable = userKeyService.disableKey(id);
        ThrowUtils.throwIf(!disable,ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 userId和accessKey 获取
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserKey> getUserKeyFromUserIdAndAccessKey(GetSecretKeyRequest secretKeyRequest) {
        Long userId = secretKeyRequest.getUserId();
        String accessKey = secretKeyRequest.getAccessKey();
        ThrowUtils.throwIf(userId <= 0,ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StrUtil.isBlank(accessKey),ErrorCode.PARAMS_ERROR);

        UserKey userKey = userKeyService.getSecretKeyFromUserIdAndAccessKey(userId,accessKey);

        if (userKey == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(userKey);
    }

    /**
     * 分页获取列表
     *
     * @param userKeyQueryRequest
     * @return
     */
//    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Page<UserKey>> listApiInfoByPage(@RequestBody UserKeyQueryRequest userKeyQueryRequest) {
//        long current = userKeyQueryRequest.getCurrent();
//        long size = userKeyQueryRequest.getPageSize();
//        Page<UserKey> apiInfoPage = userKeyService.page(new Page<>(current, size),
//                userKeyService.getQueryWrapper(userKeyQueryRequest));
//        return ResultUtils.success(apiInfoPage);
//    }

    /**
     * 分页获取列表（封装类）
     *
     * @param userKeyQueryRequest
     * @param request
     * @return
     */
//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<UserKey>> listApiInfoVOByPage(@RequestBody UserKeyQueryRequest userKeyQueryRequest,
//                                                               HttpServletRequest request) {
//        long current = userKeyQueryRequest.getCurrent();
//        long size = userKeyQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<UserKey> userKeyPage = userKeyService.page(new Page<>(current, size),
//                userKeyService.getQueryWrapper(userKeyQueryRequest));
//        return ResultUtils.success(userKeyService.getUserKeyVOPage(userKeyPage, request));
//    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param userKeyQueryRequest
     * @param request
     * @return
     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<UserKey>> listMyApiInfoVOByPage(@RequestBody UserKeyQueryRequest userKeyQueryRequest,
//                                                                 HttpServletRequest request) {
//        if (userKeyQueryRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        User loginUser = userService.getLoginUser(request);
//        userKeyQueryRequest.setUserId(loginUser.getId());
//        long current = userKeyQueryRequest.getCurrent();
//        long size = userKeyQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<UserKey> apiInfoPage = userKeyService.page(new Page<>(current, size),
//                userKeyService.getQueryWrapper(userKeyQueryRequest));
//        return ResultUtils.success(userKeyService.getUserKeyVOPage(apiInfoPage, request));
//    }


    
}
