package com.zhaozhong.openAPI.controller;
import com.zhaozhong.openAPI.common.BaseResponse;
import com.zhaozhong.openAPI.common.ErrorCode;
import com.zhaozhong.openAPI.common.ResultUtils;
import com.zhaozhong.openAPI.model.dto.apiParam.APiParamAddReq;
import com.zhaozhong.openAPI.model.dto.apiParam.ApiParamListResp;
import com.zhaozhong.openAPI.service.ApiParamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/param")
public class ApiParamController {

    @Resource
    ApiParamService apiParamService;


    @GetMapping("/list")
    public BaseResponse<List<ApiParamListResp>> list(Long apiId){
         List<ApiParamListResp> list = apiParamService.list(apiId);
         return ResultUtils.success(list);
    }

    @PostMapping("/add")
    public BaseResponse add(APiParamAddReq req){
        boolean add = apiParamService.add(req);
        return add?ResultUtils.success():ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

}
