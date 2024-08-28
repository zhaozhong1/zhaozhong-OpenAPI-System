package com.zhaozhong.openAPI.model.dto.apiParam;

import lombok.Data;

@Data
public class ApiParamListResp {

    /**
     * id
     */
    private Long id;

    /**
     * api id
     */
    private Long apiId;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数数据类型
     */
    private String paramType;

    /**
     * 是否必传，0不必传 1必传
     */
    private Integer isNeed;

}
