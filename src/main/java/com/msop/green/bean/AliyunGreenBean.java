package com.msop.green.bean;

import lombok.Data;

/**
 * @author 冷益飞
 * 内容检测场景
 */
@Data
public class AliyunGreenBean {
    /**
     * 图片检测的场景:["porn","terrorism","ad","qrcode","live","logo"]
     * 回调结果说明
     * 图片智能监黄            porn
     * 图片暴恐涉政识别        terrorism
     * 图文违规识别            ad
     * 图片二维码识别          qrcode
     * 图片不良场景识别        live
     * 图片logo识别           logo
     */
    private String[] imageScenes;
    /**
     * 文本检测场景["antispam"]
     * 回调结果说明
     * antispam => [normal：正常文本,
     * spam：含垃圾信息,
     * ad：广告,
     * politics：涉政,
     * terrorism：暴恐,
     * abuse：辱骂,
     * porn：色情,
     * flood：灌水,
     * contraband：违禁,
     * meaningless：无意义,
     * customized：自定义（比如命中自定义关键词）]
     */
    private String[] textScenes;
}
