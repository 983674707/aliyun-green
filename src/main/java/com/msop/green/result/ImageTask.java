package com.msop.green.result;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

/**
 * @author 冷益飞
 * 图片任务
 */
@Data
@ToString
public class ImageTask {

    private String dataId = UUID.randomUUID().toString();
    private String url;
    private Date time = new Date();

    public ImageTask(String url) {
        this.url = url;
    }

}
