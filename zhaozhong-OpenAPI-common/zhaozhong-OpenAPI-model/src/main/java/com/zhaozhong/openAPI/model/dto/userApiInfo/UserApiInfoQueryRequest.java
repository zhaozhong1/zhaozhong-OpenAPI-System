package com.zhaozhong.openAPI.model.dto.userApiInfo;


import com.zhaozhong.openAPI.model.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApiInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键自增
     */
    private Long id;

    /**
     * 调用用户id
     */
    private Long userId;
    /**
     * 接口id
     */
    private Long apiInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;
    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 状态
     */
    private Integer status;

}
