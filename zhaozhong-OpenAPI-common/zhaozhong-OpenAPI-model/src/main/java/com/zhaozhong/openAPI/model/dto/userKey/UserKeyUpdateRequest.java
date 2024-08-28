package com.zhaozhong.openAPI.model.dto.userKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserKeyUpdateRequest {


    private Long userId;

    private String accessKey;


    /**
     * 是否激活 0激活 1非激活
     */
    private Integer isActive;

}
