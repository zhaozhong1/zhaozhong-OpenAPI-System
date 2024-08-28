package com.zhaozhong.openAPI.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhaozhong.OpenAPI.innerService.InnerUserKeyService;
import com.zhaozhong.openAPI.common.ErrorCode;
import com.zhaozhong.openAPI.constant.DBConstant;
import com.zhaozhong.openAPI.exception.ThrowUtils;
import com.zhaozhong.openAPI.model.dto.userKey.UserKeyQueryRequest;
import com.zhaozhong.openAPI.model.entity.UserKey;
import com.zhaozhong.openAPI.service.UserKeyService;
import com.zhaozhong.openAPI.mapper.UserKeyMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
* @author 22843
* @description 针对表【user_key(用户ak/sk列表)】的数据库操作Service实现
* @createDate 2024-01-25 16:29:48
*/
@Service
@DS(DBConstant.USER_CENTER)
public class UserKeyServiceImpl extends ServiceImpl<UserKeyMapper, UserKey>
    implements UserKeyService, InnerUserKeyService {

    private final String SALT_FOR_KEY = "zahkosnkg";
    @Override
    public void validGenerator(String generator) {
        ThrowUtils.throwIf(generator.length()<6||generator.length()>20, ErrorCode.PARAMS_ERROR);
    }

    @Override
    public String generateAccessKey(String generator,Long userId){
        String randomString = RandomUtil.randomString(10);
        return getDigestString(generator, userId, randomString);
    }

    @Override
    public String generateSecretKey(String generator,Long userId,String accessKey){
        return getDigestString(generator, userId, accessKey);
    }

    @Override
    public UserKey getSecretKeyFromUserIdAndAccessKey(Long userId, String accessKey) {

        UserKeyQueryRequest userKeyQueryRequest = new UserKeyQueryRequest();
        userKeyQueryRequest.setUserId(userId);
        userKeyQueryRequest.setAccessKey(accessKey);
        QueryWrapper<UserKey> userApiInfoQueryWrapper = this.getQueryWrapper(userKeyQueryRequest);
        UserKey userKey = this.getOne(userApiInfoQueryWrapper);
        ThrowUtils.throwIf(userKey == null,ErrorCode.PARAMS_ERROR);
        return userKey;
    }

    @Override
    public QueryWrapper<UserKey> getQueryWrapper(UserKeyQueryRequest userKeyQueryRequest) {
        QueryWrapper<UserKey> queryWrapper = new QueryWrapper<>();
        if (userKeyQueryRequest == null) {
            return queryWrapper;
        }
        Long id = userKeyQueryRequest.getId();
        Long userId = userKeyQueryRequest.getUserId();
        String accessKey = userKeyQueryRequest.getAccessKey();
        String secretKey = userKeyQueryRequest.getSecretKey();
        Integer isActive = userKeyQueryRequest.getIsActive();
        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId),"user_id",userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(accessKey),"access_key",accessKey);
        queryWrapper.eq(ObjectUtils.isNotEmpty(secretKey),"secret_key",secretKey);
        queryWrapper.eq(ObjectUtils.isNotEmpty(isActive),"is_active",isActive);
        return queryWrapper;
    }

    @Override
    public Long getUserIdByKey(String accessKey) {
        ThrowUtils.throwIf(StrUtil.isBlank(accessKey),ErrorCode.PARAMS_ERROR);
        UserKeyQueryRequest userKeyQueryRequest = new UserKeyQueryRequest();
        userKeyQueryRequest.setAccessKey(accessKey);

        QueryWrapper<UserKey> queryWrapper = getQueryWrapper(userKeyQueryRequest);
        UserKey userKey = this.getOne(queryWrapper);
        ThrowUtils.throwIf(userKey == null,ErrorCode.NOT_FOUND_ERROR);
        return userKey.getUserId();
    }

    @Nullable
    private String getDigestString(String generator, Long userId, String body) {
        String input = userId + generator + body + SALT_FOR_KEY;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过ak获取sk(网关远程调用)
     * @param accessKey
     * @return
     */
    @Override
    public String getSecretKeyByAccessKey(String accessKey) {
        ThrowUtils.throwIf(StrUtil.isBlank(accessKey),ErrorCode.PARAMS_ERROR);
        UserKeyQueryRequest userKeyQueryRequest = new UserKeyQueryRequest();
        userKeyQueryRequest.setAccessKey(accessKey);
        QueryWrapper<UserKey> queryWrapper = this.getQueryWrapper(userKeyQueryRequest);
        UserKey userKey = this.getOne(queryWrapper);
        ThrowUtils.throwIf(userKey == null,ErrorCode.NOT_FOUND_ERROR);
        return userKey.getSecretKey();
    }

    @Override
    public Boolean enableKey(Long id) {
        UpdateWrapper<UserKey> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.setSql("is_active = 0");
        return this.update(updateWrapper);
    }

    @Override
    public Boolean disableKey(Long id) {
        UpdateWrapper<UserKey> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.setSql("is_active = 1");
        return this.update(updateWrapper);
    }
}




