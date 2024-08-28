package com.zhaozhong.openAPI.innerService;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zhaozhong.OpenAPI.innerService.InnerApiInfoService;
import com.zhaozhong.openAPI.constant.DBConstant;
import com.zhaozhong.openAPI.mapper.ApiInfoMapper;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@DubboService
public class InnerApiInfoServiceImpl implements InnerApiInfoService {


    @Resource
    ApiInfoMapper apiInfoMapper;

    @DS(DBConstant.API_PLATFORM)
    @Override
    public List<ApiInfo> getApiInfoList() {
        return apiInfoMapper.selectList(null);
    }
}
