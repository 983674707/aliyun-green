package com.msop.green.result;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author 冷益飞
 * 检测回调结果
 */
@Data
@ToString
public class CheckResult {

    /**
     * 当前任务id
     */
    public String taskId;
    /**
     * 鉴定对象的地址
     */
    public String url;
    /**
     * 描述 如：ok
     */
    public String msg;
    /**
     * 状态
     */
    public Integer code;
    /**
     * 数据Id。需要保证每一次请求中所有的Id不重复。
     */
    public String dataId;
    /**
     * 额外调用参数
     */
    public String extras;
    /**
     * 鉴定结果
     */
    public List<SceneDetail> results;

}
