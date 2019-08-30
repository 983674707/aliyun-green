package com.msop.green.bean;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author 冷益飞
 * sdk 配置
 */
@Data
public class AliyunSdkBean {
    private String accessKeyId;
    private String accessKeySecret;
    /**
     * cn-shanghai或cn-beijing
     */
    private String regionId = "cn-beijing";
}
