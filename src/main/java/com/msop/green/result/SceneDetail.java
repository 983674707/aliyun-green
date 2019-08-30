package com.msop.green.result;

import lombok.Data;
import lombok.ToString;

/**
 * @author 冷益飞
 * 回调详情
 */
@Data
@ToString
public class SceneDetail {

    /**
     * 确定概率
     */
    private Double rate;
    /**
     * 检测场景
     */
    private String scene;
    /**
     * 建议的操作
     *  pass:正常
     *  review:需人工审核
     *  block:违规
     */
    private String suggestion;
    /**
     * 结果分类
     * 图片：
     *         porn（图片智能监黄）：
     *              normal：正常图片，无色情内容
     *              sexy：性感图片
     *              porn：色情图片
     *
     *         terrorism（图片暴恐涉政识别）：
     *              normal：正常图片
     *              bloody：血腥
     *              explosion：爆炸烟光
     *              outfit：特殊装束
     *              logo：特殊标识
     *              weapon：武器
     *              politics：涉政
     *              violence ： 打斗
     *              crowd：聚众
     *              parade：游行
     *              carcrash：车祸现场
     *              others：其他
     *
     * 文本：
     *          antispam（文本）：
     *              normal：正常文本
     *              spam：含垃圾信息
     *              ad：广告
     *              politics：涉政
     *              terrorism：暴恐
     *              abuse：辱骂
     *              porn：色情
     *              flood：灌水
     *              contraband：违禁
     *              meaningless：无意义
     *              customized：自定义（比如命中自定义关键词）
     */
    private String label;
    private String error;

}
