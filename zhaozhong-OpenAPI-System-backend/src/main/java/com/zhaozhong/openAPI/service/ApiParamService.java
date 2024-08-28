package com.zhaozhong.openAPI.service;

import com.zhaozhong.openAPI.model.dto.apiParam.APiParamAddReq;
import com.zhaozhong.openAPI.model.dto.apiParam.ApiParamListResp;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ApiParamService {

    List<ApiParamListResp> list(Long apiId);

    boolean add(APiParamAddReq req);
}
