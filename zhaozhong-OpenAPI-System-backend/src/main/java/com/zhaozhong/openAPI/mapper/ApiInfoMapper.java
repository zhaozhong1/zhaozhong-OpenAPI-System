package com.zhaozhong.openAPI.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhaozhong.openAPI.model.entity.ApiInfo;

import java.util.List;

/**
* @author 22843
* @description 针对表【api_info】的数据库操作Mapper
* @createDate 2024-01-04 10:04:12
* @Entity com.zhaozhong.openAPI.model.entity.ApiInfo
*/
public interface ApiInfoMapper extends BaseMapper<ApiInfo> {
    List<ApiInfo> testPage(Integer current, Integer size);
}




