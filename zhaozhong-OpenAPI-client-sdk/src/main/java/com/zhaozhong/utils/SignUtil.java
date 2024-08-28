package com.zhaozhong.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class SignUtil {

    public static String genSign(String sk,String body){
        log.info("接收到body：{}",body);
        if(StrUtil.isBlank(sk))throw new RuntimeException("数据不可为空！");
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        return md5.digestHex(body +"."+sk);
    }

}
