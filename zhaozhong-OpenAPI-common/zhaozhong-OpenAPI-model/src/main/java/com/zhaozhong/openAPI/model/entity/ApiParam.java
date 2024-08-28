package com.zhaozhong.openAPI.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * api参数表
 * @TableName api_param
 */
@TableName(value ="api_param")
@Data
public class ApiParam implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ApiParam other = (ApiParam) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getApiId() == null ? other.getApiId() == null : this.getApiId().equals(other.getApiId()))
            && (this.getParamName() == null ? other.getParamName() == null : this.getParamName().equals(other.getParamName()))
            && (this.getParamType() == null ? other.getParamType() == null : this.getParamType().equals(other.getParamType()))
            && (this.getIsNeed() == null ? other.getIsNeed() == null : this.getIsNeed().equals(other.getIsNeed()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getApiId() == null) ? 0 : getApiId().hashCode());
        result = prime * result + ((getParamName() == null) ? 0 : getParamName().hashCode());
        result = prime * result + ((getParamType() == null) ? 0 : getParamType().hashCode());
        result = prime * result + ((getIsNeed() == null) ? 0 : getIsNeed().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", apiId=").append(apiId);
        sb.append(", paramName=").append(paramName);
        sb.append(", paramType=").append(paramType);
        sb.append(", isNeed=").append(isNeed);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}