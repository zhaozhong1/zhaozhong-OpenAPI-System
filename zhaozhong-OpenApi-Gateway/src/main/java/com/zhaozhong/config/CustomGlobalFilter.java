package com.zhaozhong.config;

import cn.hutool.core.util.StrUtil;
import com.zhaozhong.OpenAPI.innerService.InnerUserApiInfoService;
import com.zhaozhong.OpenAPI.innerService.InnerUserKeyService;
import com.zhaozhong.OpenAPI.innerService.InnerUserService;
import com.zhaozhong.openAPI.model.entity.UserApiInfo;
import com.zhaozhong.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    InnerUserApiInfoService innerUserApiInfoService;
    @DubboReference
    InnerUserKeyService innerUserKeyService;

    public static final List<String> IP_WHITE_LIST = List.of("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 1. 设置请求日志
        String sourceHostIP = request.getRemoteAddress().getHostString();
        logRequest(request);

        // 2. 黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if(!IP_WHITE_LIST.contains(sourceHostIP)){
            return handleNoAuth(response);
        }

        // HttpHeaders继承了一个Map，可以当做hashMap
        HttpHeaders headersMap = request.getHeaders();
        String apiUri = request.getURI().toString();
        String regex = "https?:\\/\\/[^\\/]+(\\/[^?]*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(apiUri);
        String path = null;
        if (matcher.find()) {
            path = matcher.group(1);
        }
        // 3. 判断请求接口和用户是否存在
        // 3.1 获取客户端传过来的ak
        String accessKey = headersMap.getFirst("accessKey");
        UserApiInfo invokeUserApiInfo = innerUserApiInfoService.getInvokeUserApiInfo(accessKey, path);
        if(invokeUserApiInfo.getUserId() == null){
            // 用户不存在
            // TODO 定义新方法处理用户不存在
            return handleNoAuth(response);
        }
        if(invokeUserApiInfo.getApiInfoId() == null){
            // 接口不存在
            // TODO 定义新方法处理接口不存在
            return handleNoAuth(response);
        }
        if(invokeUserApiInfo.getLeftNum()<=0){
            // 用户调用接口次数不足
            return handleNoAuth(response);
        }
        // TODO 比较方法和参数是否正确
        // 4. 统一用户鉴权（ak/sk）
        if(!authentication(headersMap)){
            return handleNoAuth(response);
        }
        Long userId = invokeUserApiInfo.getUserId();
        Long apiInfoId = invokeUserApiInfo.getApiInfoId();

        // 剩余步骤都在该函数中进行
        return aroundInvokeApi(exchange, chain, userId,apiInfoId);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 处理没有权限调用的情况，如不在白名单内、鉴权失败、调用失败等
     * @param response 响应体
     */
    private Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
    /**
     * 处理服务器错误的情况
     * @param response 响应体
     * @param httpStatus 返回的状态码
     */
    private Mono<Void> handleError(ServerHttpResponse response,HttpStatus httpStatus){
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    /**
     * 统一鉴权方法
     */
    private boolean authentication(HttpHeaders headersMap){
        String ak = headersMap.getFirst("accessKey");
        String nonce = headersMap.getFirst("nonce");
        String body = headersMap.getFirst("body");
        String timestamp = headersMap.getFirst("timestamp");
        if(StrUtil.isBlank(ak)){
            log.info("access key为空。");
            return false;
        }
        if(StrUtil.isBlank(nonce)){
            log.info("随机数为空。");
            return false;
        }
        if(StrUtil.isBlank(timestamp)){
            log.info("时间戳为空。");
            return false;
        }
        // 校验nonce是否合法
        if(nonce.length() != 4 || !StrUtil.isNumeric(nonce)){
            // 不合法
            log.info("随机数不合法。");
            return false;
        }
        // 校验时间戳是否到时（超时阈值为5分钟）
        final long FIVE_MINUTE = 60 * 5L;
        long currentTime = System.currentTimeMillis() / 1000;
        if((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTE){
            log.info("签名已过期，需重新生成。");
            return false;
        }
        String clientSign = headersMap.getFirst("sign");
        if(StrUtil.isBlank(clientSign)){
            log.info("secret key为空。");
            return false;
        }
        // mySk从数据库中获取
        String mySk= innerUserKeyService.getSecretKeyByAccessKey(ak);
        String serverSign = SignUtil.genSign(mySk,body);
        if(!clientSign.equals(serverSign)){
            log.info("根据用户参数生成的sk与数据库sk不匹配，校验失败。");
            return false;
        }
        return true;
    }

    /**
     * 统一请求日志
     * @param request
     */
    private void logRequest(ServerHttpRequest request){
        String sourceHostIP = request.getRemoteAddress().getHostString();
        log.info("请求ID："+request.getId());
        log.info("请求方法："+request.getMethod());
        log.info("请求参数："+request.getQueryParams());
        log.info("请求来源主机："+ sourceHostIP);
    }

    /**
     * 统一响应日志
     */
    private Mono<Void> aroundInvokeApi(ServerWebExchange exchange, GatewayFilterChain chain,Long userId,Long apiInfoId){
        try {
            // 调用前
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    /**
                     * 该方法是在执行完接口代码后再执行的日志方法，其中将缓冲区中的字节数组组装字符串并输出。
                     */
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        // 调用后
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody= Flux.from(body);
                            // 这一部分是进行组装字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 6. 响应日志
                                // 7. 调用接口：成功，接口调用次数+1，接口剩余调用次数-1
                                innerUserApiInfoService.invokeCount(apiInfoId,userId);
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8. 调用失败，返回一个规范错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 5. 请求转发(调用接口)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

}