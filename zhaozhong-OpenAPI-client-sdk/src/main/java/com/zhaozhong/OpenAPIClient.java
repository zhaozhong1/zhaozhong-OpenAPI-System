package com.zhaozhong;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhaozhong.model.User;
import com.zhaozhong.openAPI.model.dto.apiParam.ApiParamListResp;
import com.zhaozhong.openAPI.model.entity.ApiInfo;
import com.zhaozhong.utils.SignUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAPIClient {

    protected String myAk;
    protected String mySk;
    public final String GATEWAY_HOST = "127.0.0.1:8090";

    protected HashMap<String, String> getHeadersMap(String baseHeaders,String params) {
        HashMap<String, String> map = new HashMap<>();
        JSONObject reqHeader = JSONUtil.parseObj(baseHeaders);
        reqHeader.forEach((k, v) -> map.put(k, (String) v));
        String encodeBody = Base64.getEncoder().encodeToString(params.getBytes());
        map.put("accessKey", myAk);
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("body", encodeBody);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("sign", SignUtil.genSign(mySk, encodeBody));
        return map;
    }

    /**
     * 供外部服务调用的方法
     *
     * @param user
     * @return
     */
    public String getApi(User user) {
        return null;
    }

    /**
     * @param apiInfo api相关信息
     * @param list api参数列表
     * @param requestParams api传参
     * @param file 文件(非必传)
     * @param contentType 请求contentType
     * @return 响应体
     */
    public String sendPOSTRequest(ApiInfo apiInfo, List<ApiParamListResp> list, Map<String,Object> requestParams, MultipartFile file, ContentType contentType) throws IOException {
        String path = apiInfo.getPath();
        String reqHeaderMap = apiInfo.getReqHeader();
        if(StrUtil.isBlank(reqHeaderMap)){
            reqHeaderMap = "{}";
        }
        // 封装头部信息
        HashMap<String, String> headersMap = getHeadersMap(reqHeaderMap,JSONUtil.toJsonStr(requestParams));
        headersMap.put("Content-type",contentType.getValue());
        // 将 api 中规定的请求头放入请求头map一并发送
        HttpRequest httpRequest = HttpRequest
                .post(GATEWAY_HOST + path)
                .addHeaders(headersMap);
        // 选择发送的content
        switch (contentType){
            case FORM_URLENCODED:
                httpRequest = httpRequest.form(requestParams);
                break;
            case MULTIPART:
                httpRequest.form(requestParams).form("file",file.getBytes(),file.getOriginalFilename());
                break;
            case JSON:
                httpRequest = httpRequest.body(JSONUtil.toJsonStr(requestParams));
                break;
        }
        HttpResponse response = httpRequest.execute();
        return response.getStatus() == 200 ? response.body() : "error";
    }

    public String sendGETRequest(ApiInfo apiInfo, Map<String,String> requestParam) {
        String uri = apiInfo.getUri();
        String reqHeaderMap = apiInfo.getReqHeader();
        HashMap<String, String> headersMap = getHeadersMap(reqHeaderMap,JSONUtil.toJsonStr(reqHeaderMap));
        HttpResponse response = HttpRequest.get(GATEWAY_HOST + uri)
                .addHeaders(headersMap)
                .execute();
        return null;
    }

}
