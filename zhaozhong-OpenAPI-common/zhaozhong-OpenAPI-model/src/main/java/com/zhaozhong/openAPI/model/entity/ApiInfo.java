package com.zhaozhong.openAPI.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName api_info
 */
@TableName(value ="api_info")
@Data
public class ApiInfo implements Serializable {
    /**
     * 主键自增
     */
    @TableId(type = IdType.AUTO)
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
     * 资源路径
     */
    private String path;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除 0-未删 1-已删
     */
    private Integer isDelete;

    /**
     * 接口主机地址
     */
    private String host;

    /**
     * 资源路径
     */
    private String uri;

    /**
     * 数据类型
     */
    private String contentType;

    /**
     * 路由ID
     */
    private String routeId;

    /**
     * 路由断言规则
     */
    private String predicates;

    /**
     * 路由过滤器配置
     */
    private String filters;

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
        ApiInfo other = (ApiInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getApiName() == null ? other.getApiName() == null : this.getApiName().equals(other.getApiName()))
            && (this.getApiDescription() == null ? other.getApiDescription() == null : this.getApiDescription().equals(other.getApiDescription()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getMethod() == null ? other.getMethod() == null : this.getMethod().equals(other.getMethod()))
            && (this.getReqHeader() == null ? other.getReqHeader() == null : this.getReqHeader().equals(other.getReqHeader()))
            && (this.getRespHeader() == null ? other.getRespHeader() == null : this.getRespHeader().equals(other.getRespHeader()))
            && (this.getApiStatus() == null ? other.getApiStatus() == null : this.getApiStatus().equals(other.getApiStatus()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getHost() == null ? other.getHost() == null : this.getHost().equals(other.getHost()))
            && (this.getUri() == null ? other.getUri() == null : this.getUri().equals(other.getUri()))
            && (this.getContentType() == null ? other.getContentType() == null : this.getContentType().equals(other.getContentType()))
            && (this.getRouteId() == null ? other.getRouteId() == null : this.getRouteId().equals(other.getRouteId()))
            && (this.getPredicates() == null ? other.getPredicates() == null : this.getPredicates().equals(other.getPredicates()))
            && (this.getFilters() == null ? other.getFilters() == null : this.getFilters().equals(other.getFilters()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getApiName() == null) ? 0 : getApiName().hashCode());
        result = prime * result + ((getApiDescription() == null) ? 0 : getApiDescription().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result = prime * result + ((getReqHeader() == null) ? 0 : getReqHeader().hashCode());
        result = prime * result + ((getRespHeader() == null) ? 0 : getRespHeader().hashCode());
        result = prime * result + ((getApiStatus() == null) ? 0 : getApiStatus().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getHost() == null) ? 0 : getHost().hashCode());
        result = prime * result + ((getUri() == null) ? 0 : getUri().hashCode());
        result = prime * result + ((getContentType() == null) ? 0 : getContentType().hashCode());
        result = prime * result + ((getRouteId() == null) ? 0 : getRouteId().hashCode());
        result = prime * result + ((getPredicates() == null) ? 0 : getPredicates().hashCode());
        result = prime * result + ((getFilters() == null) ? 0 : getFilters().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", apiName=").append(apiName);
        sb.append(", apiDescription=").append(apiDescription);
        sb.append(", path=").append(path);
        sb.append(", method=").append(method);
        sb.append(", reqHeader=").append(reqHeader);
        sb.append(", respHeader=").append(respHeader);
        sb.append(", apiStatus=").append(apiStatus);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", host=").append(host);
        sb.append(", uri=").append(uri);
        sb.append(", contentType=").append(contentType);
        sb.append(", routeId=").append(routeId);
        sb.append(", predicates=").append(predicates);
        sb.append(", filters=").append(filters);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}