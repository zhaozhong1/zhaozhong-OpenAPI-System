package com.zhaozhong.openAPI.model.dto.userKey;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class UserKeyQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * user_id
     */
    private Long userId;

    /**
     * access_key
     */
    private String accessKey;

    /**
     * secret_key
     */
    private String secretKey;

    /**
     * 是否激活 0激活 1非激活
     */
    private Integer isActive;

}
