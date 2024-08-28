package com.zhaozhong.openAPI.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhaozhong.openAPI.constant.DBConstant;
import com.zhaozhong.openAPI.mapper.ApiParamMapper;
import com.zhaozhong.openAPI.model.dto.apiParam.APiParamAddReq;
import com.zhaozhong.openAPI.model.dto.apiParam.ApiParamListResp;
import com.zhaozhong.openAPI.model.entity.ApiParam;
import com.zhaozhong.openAPI.service.ApiParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@DS(DBConstant.API_PLATFORM)
@Service
public class ApiParamServiceImpl implements ApiParamService {
    @Resource
    ApiParamMapper apiParamMapper;

    @Override
    public List<ApiParamListResp> list(Long apiId) {
        List<ApiParam> apiParamList = apiParamMapper.selectList(new LambdaQueryWrapper<ApiParam>().eq(ApiParam::getApiId, apiId));
        List<ApiParamListResp> respList = new ArrayList<>();
        apiParamList.forEach(record-> respList.add(BeanUtil.copyProperties(record,ApiParamListResp.class)));
        return respList;
    }

    @Override
    @Transactional
    public boolean add(APiParamAddReq req) {
        ApiParam apiParam = new ApiParam();
        apiParam.setApiId(req.getApiId());
        apiParam.setParamName(req.getParamName());
        apiParam.setParamType(req.getParamType());
        apiParam.setIsNeed(req.getIsNeed());
        int insert = apiParamMapper.insert(apiParam);
        return insert >= 1;
    }

}
