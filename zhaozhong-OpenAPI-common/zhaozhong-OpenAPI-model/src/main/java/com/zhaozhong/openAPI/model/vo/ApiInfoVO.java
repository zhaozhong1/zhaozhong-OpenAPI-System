package com.zhaozhong.openAPI.model.vo;

import com.zhaozhong.openAPI.model.entity.ApiInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 帖子视图
 *

 */
@Data
public class ApiInfoVO implements Serializable {

    /**
     * 主键自增
     */
    private Long id;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口描述
     */
    private String apiDescription;

    /**
     * 接口地址
     */
    private String apiUrl;

    /**
     * 接口主机地址
     */
    private String host;

    /**
     * 资源路径
     */
    private String uri;


    /**
     * 方接口法
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
     * 接口状态 0-关闭 1-开启
     */
    private Integer apiStatus;

    /**
     * 创建人
     */
    private Long userId;
    /**
     * 接口总共调用次数
     */
    private Integer totalNum;

    /**
     * 包装类转对象
     *
     * @param apiInfoVO
     * @return
     */
    public static ApiInfo voToObj(ApiInfoVO apiInfoVO) {
        if (apiInfoVO == null) {
            return null;
        }
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(apiInfoVO, apiInfo);
        return apiInfo;
    }

    /**
     * 对象转包装类
     *
     * @param apiInfo
     * @return
     */
    public static ApiInfoVO objToVo(ApiInfo apiInfo) {
        if (apiInfo == null) {
            return null;
        }
        ApiInfoVO apiInfoVO = new ApiInfoVO();
        BeanUtils.copyProperties(apiInfo, apiInfoVO);
        return apiInfoVO;
    }
}
