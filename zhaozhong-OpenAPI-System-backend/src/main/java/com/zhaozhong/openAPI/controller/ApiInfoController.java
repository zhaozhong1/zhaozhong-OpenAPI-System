package com.zhaozhong.openAPI.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaozhong.OpenAPIClient;
import com.zhaozhong.openAPI.annotation.AuthCheck;
import com.zhaozhong.openAPI.common.*;
import com.zhaozhong.openAPI.constant.UserConstant;
import com.zhaozhong.openAPI.exception.BusinessException;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.mapper.UserKeyMapper;
import com.zhaozhong.openAPI.model.dto.apiInfo.ApiInfoAddRequest;
import com.zhaozhong.openAPI.model.dto.apiInfo.ApiInfoQueryRequest;
import com.zhaozhong.openAPI.model.dto.apiInfo.ApiInfoUpdateRequest;
import com.zhaozhong.openAPI.model.dto.apiParam.ApiParamListResp;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import com.zhaozhong.openAPI.model.entity.User;
import com.zhaozhong.openAPI.model.entity.UserKey;
import com.zhaozhong.openAPI.model.enums.ApiInfoStatusEnum;
import com.zhaozhong.openAPI.model.vo.ApiInfoVO;
import com.zhaozhong.openAPI.service.ApiInfoService;
import com.zhaozhong.openAPI.service.ApiParamService;
import com.zhaozhong.openAPI.service.UserApiInfoService;
import com.zhaozhong.openAPI.service.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 帖子接口
 *

 */
@RestController
@RequestMapping("/apiInfo")
@Slf4j
public class ApiInfoController {

    @Resource
    private ApiInfoService apiInfoService;

    @Resource
    private UserService userService;

    @Resource
    private OpenAPIClient openAPIClient;

    @Resource
    private UserApiInfoService userApiInfoService;
    // region 增删改查

    @Resource
    private UserKeyMapper userKeyMapper;

    @Resource
    private ApiParamService apiParamService;

    /**
     * 创建
     *
     * @param apiInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addApiInfo(@RequestBody ApiInfoAddRequest apiInfoAddRequest, HttpServletRequest request) {
        if (apiInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(apiInfoAddRequest, apiInfo);

        apiInfoService.validApiInfo(apiInfo, true);
        User loginUser = userService.getLoginUser(request);
        apiInfo.setUserId(loginUser.getId());

        boolean result = apiInfoService.save(apiInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newApiInfoId = apiInfo.getId();
        return ResultUtils.success(newApiInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteApiInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ApiInfo oldApiInfo = apiInfoService.getById(id);
        ThrowUtils.throwIf(oldApiInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldApiInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = apiInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param apiInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateApiInfo(@RequestBody ApiInfoUpdateRequest apiInfoUpdateRequest) {
        if (apiInfoUpdateRequest == null || apiInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(apiInfoUpdateRequest, apiInfo);

        // 参数校验
        apiInfoService.validApiInfo(apiInfo, false);
        long id = apiInfoUpdateRequest.getId();
        // 判断是否存在
        ApiInfo oldApiInfo = apiInfoService.getById(id);
        ThrowUtils.throwIf(oldApiInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = apiInfoService.updateById(apiInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<ApiInfo> getApiInfoVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ApiInfo apiInfo = apiInfoService.getById(id);
        if (apiInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(apiInfo);
    }

    /**
     * 分页获取列表
     *
     * @param apiInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ApiInfo>> listApiInfoByPage(@RequestBody ApiInfoQueryRequest apiInfoQueryRequest) {
        long current = apiInfoQueryRequest.getCurrent();
        long size = apiInfoQueryRequest.getPageSize();
        Page<ApiInfo> apiInfoPage = apiInfoService.page(new Page<>(current, size),
                apiInfoService.getQueryWrapper(apiInfoQueryRequest));
        return ResultUtils.success(apiInfoPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param apiInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ApiInfoVO>> listApiInfoVOByPage(@RequestBody ApiInfoQueryRequest apiInfoQueryRequest,
            HttpServletRequest request) {
        long current = apiInfoQueryRequest.getCurrent();
        long size = apiInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ApiInfo> apiInfoPage = apiInfoService.page(new Page<>(current, size),
                apiInfoService.getQueryWrapper(apiInfoQueryRequest));
        return ResultUtils.success(apiInfoService.getApiInfoVOPage(apiInfoPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param apiInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ApiInfoVO>> listMyApiInfoVOByPage(@RequestBody ApiInfoQueryRequest apiInfoQueryRequest,
                                                               HttpServletRequest request) {
        if (apiInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        apiInfoQueryRequest.setUserId(loginUser.getId());
        long current = apiInfoQueryRequest.getCurrent();
        long size = apiInfoQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ApiInfo> apiInfoPage = apiInfoService.page(new Page<>(current, size),
                apiInfoService.getQueryWrapper(apiInfoQueryRequest));
        return ResultUtils.success(apiInfoService.getApiInfoVOPage(apiInfoPage, request));
    }


    /**
     * 发布（仅管理员）
     *
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineApiInfo(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        Long id = idRequest.getId();
        ApiInfo apiInfo = apiInfoService.getById(id);
        // 判断接口是否存在
        ThrowUtils.throwIf(apiInfo==null,ErrorCode.NOT_FOUND_ERROR);
        // 判断接口是否能够被调用
        com.zhaozhong.model.User user = new com.zhaozhong.model.User();
        user.setUsername("zhaozhong");
        String res = openAPIClient.getApi(user);
        ThrowUtils.throwIf(res.equals("error"),ErrorCode.SYSTEM_ERROR);
        ApiInfo onLineApiInfo = new ApiInfo();
        onLineApiInfo.setId(id);
        onLineApiInfo.setApiStatus(ApiInfoStatusEnum.ONLINE.getValue());
        boolean result = apiInfoService.updateById(onLineApiInfo);
        return ResultUtils.success(result);
    }
    /**
     * 下线（仅管理员）
     *
     * @param idRequest 接口id
     * @return 布尔值
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineApiInfo(@RequestBody IdRequest idRequest,HttpServletRequest request) {
        Long id = idRequest.getId();
        ApiInfo apiInfo = apiInfoService.getById(id);
        // 判断接口是否存在
        ThrowUtils.throwIf(apiInfo==null,ErrorCode.NOT_FOUND_ERROR);

        ApiInfo onLineApiInfo = new ApiInfo();
        onLineApiInfo.setId(id);
        onLineApiInfo.setApiStatus(ApiInfoStatusEnum.OFFLINE.getValue());
        boolean result = apiInfoService.updateById(onLineApiInfo);
        return ResultUtils.success(result);
    }

    /**
     * 在线调用接口方法
     * @param params 包含了apiId 以及对应的参数列表
     * @param request accessKey，currentUser
     * @return 接口返回
     */
    @PostMapping("/invoke/post/form")
//    @AuthCheck(mustRole = UserConstant.USER_LOGIN_STATE)
    public BaseResponse<Object> invokeAPIInfo(@RequestParam Map<String, Object> params,
                                              @RequestParam(value = "file", required = false) MultipartFile file,
                                              HttpServletRequest request) throws IOException {
        Object apiId = params.get("apiId");
        // 如果请求参数为空或者请求参数携带id为空，则抛出参数错误异常
        ThrowUtils.throwIf(!StrUtil.isNumeric(apiId.toString()),ErrorCode.PARAMS_ERROR);
        // 获取接口信息
        ApiInfo apiInfo = apiInfoService.getById(Long.parseLong(apiId.toString()));
        OpenAPIClient tempClient = configClient(request);
        // 发送
        List<ApiParamListResp> list = apiParamService.list(Long.parseLong(apiId.toString()));
        ContentType contentType;
        if(file != null){
            contentType = ContentType.MULTIPART;
        }else{
            contentType = ContentType.FORM_URLENCODED;
        }
        String res = tempClient.sendPOSTRequest(apiInfo,list,params,file, contentType);
        return ResultUtils.success(res);
    }

    private OpenAPIClient configClient(HttpServletRequest request){
        // 创建客户端
        OpenAPIClient tempClient = new OpenAPIClient();
        // ak/sk 可以从用户的其中一个启用的拿
        UserKey userKey = userKeyMapper.getAkSkByUserId(userService.getLoginUser(request).getId());
        String accessKey = userKey.getAccessKey();
        String secretKey = userKey.getSecretKey();
        tempClient.setMyAk(accessKey);
        tempClient.setMySk(secretKey);
        return tempClient;
    }

}
