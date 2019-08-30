package com.msop.green.auto;

import com.msop.green.bean.AliyunGreenBean;
import com.msop.green.bean.AliyunSdkBean;
import com.msop.green.template.GreenTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 冷益飞
 */
@ConditionalOnProperty(prefix = "aliyun.sdk", name = "enabled", havingValue = "true")
public class AliyunGreenAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "aliyun.sdk")
    public AliyunSdkBean aliyunSdkBean() {
        return new AliyunSdkBean();
    }

    @Bean
    @ConfigurationProperties(prefix = "aliyun.sdk.green")
    public AliyunGreenBean aliyunGreenBean() {
        return new AliyunGreenBean();
    }

    @Bean
    public GreenTemplate greenTemplate(AliyunSdkBean aliyunSdkBean, AliyunGreenBean aliyunGreenBean) {
        return new GreenTemplate(aliyunSdkBean, aliyunGreenBean);
    }

}
