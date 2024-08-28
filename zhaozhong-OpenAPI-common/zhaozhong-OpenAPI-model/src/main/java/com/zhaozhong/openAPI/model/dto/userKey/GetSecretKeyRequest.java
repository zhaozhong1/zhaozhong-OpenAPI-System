package com.zhaozhong.openAPI.model.dto.userKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSecretKeyRequest {

    private Long userId;

    private String accessKey;
}
