package com.zhaozhong.openAPI.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 */
public enum ApiInfoStatusEnum {
    ONLINE("上线",1),
    OFFLINE("下线",0);

    private final String status;
    private final int value;

    public String getStatus() {
        return status;
    }

    public int getValue() {
        return value;
    }

    public static List<Integer> getValues(){
        return  Arrays.stream(values()).map(item->item.value).collect(Collectors.toList());
    }

    ApiInfoStatusEnum(String status, int value) {
        this.status = status;
        this.value = value;
    }
}
