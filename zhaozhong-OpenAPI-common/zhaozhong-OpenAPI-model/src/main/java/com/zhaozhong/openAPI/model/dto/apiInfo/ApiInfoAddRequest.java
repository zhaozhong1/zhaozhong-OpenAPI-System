package com.zhaozhong.openAPI.model.dto.apiInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfoAddRequest {
    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 接口描述
     */
    private String apiDescription;

    /**
     * 资源路径
     */
    private String path;

    /**
     * 接口主机地址
     */
    private String host;

    /**
     * 服务地址
     */
    private String uri;

    /**
     * 接口方法
     */
    private String method;

    /**
     * 请求头
     */
    private String reqHeader;

    /**
     * 响应头
     */
    private String respHeader;

    /**
     * 断言规则 非必要
     */
    private String predicates;

}
