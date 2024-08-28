package com.zhaozhong.openAPI.model.dto.apiInfo;


import com.zhaozhong.openAPI.model.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键自增
     */
    private Long id;

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口描述
     */
    private String apiDescription;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 接口方法
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;
    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String reqHeader;

    /**
     * 响应头
     */
    private String respHeader;


}
