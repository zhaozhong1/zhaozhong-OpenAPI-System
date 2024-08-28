package com.zhaozhong.openAPI.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户ak/sk列表
 * @TableName user_key
 */
@TableName(value ="user_key")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserKey implements Serializable {
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
     * access_key
     */
    private String secretKey;

    /**
     * 是否激活 0激活 1非激活
     */
    private Integer isActive;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}