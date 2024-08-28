package com.zhaozhong.config;

import com.zhaozhong.OpenAPI.innerService.InnerApiInfoService;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class RouteRegistrar {

    @DubboReference
    InnerApiInfoService innerApiInfoService;


    /**
     * Spring初始化时自动配置路由
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        List<ApiInfo> apiInfoList = innerApiInfoService.getApiInfoList();
        RouteLocatorBuilder.Builder routes = builder.routes();
        for (ApiInfo apiInfo : apiInfoList) {
            try{
                routes.route(
                        apiInfo.getRouteId(),
                        r -> r.path(apiInfo.getPath())
                        .uri(apiInfo.getUri())
                        );
                log.info("------------ routeId:{} ; name:{}初始化成功。",apiInfo.getRouteId(),apiInfo.getApiName());
            }catch (Exception e){
                log.error("id为{}的接口初始化失败，检查注册表。",apiInfo.getId(),e);
            }
        }
        return routes.build();
    }
}
