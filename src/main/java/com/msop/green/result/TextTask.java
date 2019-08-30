package com.msop.green.result;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

/**
 * @author 冷益飞
 * 文字任务
 */
@Data
@ToString
public class TextTask {

    public String dataId = UUID.randomUUID().toString();
    public String content;

    public TextTask(String content) {
        this.content = content;
    }
}
