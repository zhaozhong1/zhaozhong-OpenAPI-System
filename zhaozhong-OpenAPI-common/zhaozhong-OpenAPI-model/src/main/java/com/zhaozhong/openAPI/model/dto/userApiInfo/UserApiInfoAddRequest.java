package com.zhaozhong.openAPI.model.dto.userApiInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserApiInfoAddRequest {

    /**
     * 调用用户id
     */
    private Long userId;
    /**
     * 接口id
     */
    private Long apiInfoId;


    /**
     * 剩余调用次数(初始化)
     */
    private Integer leftNum;

}
