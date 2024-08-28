package com.zhaozhong.openAPI.model.dto.apiInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfoInvokeRequest {
    /**
     * 接口id
     */
    private Long apiId;

    /**
     * 请求参数 using-json
     */
    private String userRequestParams;



}
