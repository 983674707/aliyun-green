package com.msop.green.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.msop.green.bean.AliyunGreenBean;
import com.msop.green.bean.AliyunSdkBean;
import com.msop.green.result.CheckResult;
import com.msop.green.result.ImageTask;
import com.msop.green.result.TextTask;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 冷益飞
 * 内容安全
 */
@Slf4j
public class GreenTemplate {
    private final AliyunSdkBean aliyunSdkBean;
    private final AliyunGreenBean aliyunGreenBean;

    public GreenTemplate(AliyunSdkBean aliyunSdkBean, AliyunGreenBean aliyunGreenBean) {
        this.aliyunSdkBean = aliyunSdkBean;
        this.aliyunGreenBean = aliyunGreenBean;
    }

    public DefaultAcsClient getClient() {
        return new DefaultAcsClient(DefaultProfile.getProfile(
                aliyunSdkBean.getRegionId(),
                aliyunSdkBean.getAccessKeyId(),
                aliyunSdkBean.getAccessKeySecret()
        ));
    }

    /**
     * 图片检测
     *
     * @param imageList
     * @return
     * @throws ClientException
     */
    public List<CheckResult> checkImage(List<ImageTask> imageList) throws ClientException {
        String product = "Green";
        String domain = "green.cn-shanghai.aliyuncs.com";
        DefaultProfile
                .addEndpoint(aliyunSdkBean.getRegionId(), product, domain);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        // 指定api返回格式
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON);
        // 指定请求方法
        imageSyncScanRequest.setMethod(MethodType.POST);
        imageSyncScanRequest.setEncoding("utf-8");
        // 支持http和https
        imageSyncScanRequest.setProtocol(ProtocolType.HTTP);

        JSONObject httpBody = new JSONObject();
        /**
         * 设置要检测的场景, 计费是按照该处传递的场景进行
         * 一次请求中可以同时检测多张图片，每张图片可以同时检测多个风险场景，计费按照场景计算
         * 例如：检测2张图片，场景传递porn,terrorism，计费会按照2张图片鉴黄，2张图片暴恐检测计算
         * porn: porn表示色情场景检测
         */
        httpBody.put("scenes", Arrays.asList(aliyunGreenBean.getImageScenes()));

        /**
         * 设置待检测图片， 一张图片一个task，
         * 多张图片同时检测时，处理的时间由最后一个处理完的图片决定。
         * 通常情况下批量检测的平均rt比单张检测的要长, 一次批量提交的图片数越多，rt被拉长的概率越高
         * 这里以单张图片检测作为示例, 如果是批量图片检测，请自行构建多个task
         */
        httpBody.put("tasks", imageList);
        imageSyncScanRequest.setHttpContent(
                org.apache.commons.codec.binary.StringUtils.getBytesUtf8(httpBody.toJSONString()),
                "UTF-8", FormatType.JSON);

        /**
         * 请设置超时时间, 服务端全链路处理超时时间为10秒，请做相应设置
         * 如果您设置的ReadTimeout 小于服务端处理的时间，程序中会获得一个read timeout 异常
         */
        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(10000);
        HttpResponse httpResponse = null;
        DefaultAcsClient client = getClient();
        try {
            httpResponse = client.doAction(imageSyncScanRequest);
        } catch (Exception e) {
            log.error("请求发送异常", e);
        }

        // 服务端接收到请求，并完成处理返回的结果
        if (httpResponse != null && httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON.parseObject(org.apache.commons.codec.binary.StringUtils
                    .newStringUtf8(httpResponse.getHttpContent()));
            int requestCode = scrResponse.getIntValue("code");
            // 每一张图片的检测结果
            JSONArray taskResults = scrResponse.getJSONArray("data");
            if (200 == requestCode) {
                List<CheckResult> result = new LinkedList<>();
                // 所有结果
                for (Object taskResult : taskResults) {
                    // 单张图片处理结果
                    if (200 == ((JSONObject) taskResult).getIntValue("code")) {
                        CheckResult checkResult = JSONObject
                                .parseObject(JSON.toJSONString(taskResult), CheckResult.class);
                        result.add(checkResult);
                        continue;
                    }
                    CheckResult checkResult = new CheckResult();
                    checkResult.setMsg("task process fail. task response:" + JSON
                            .toJSONString(taskResult));
                    checkResult.setCode(500);
                    result.add(checkResult);
                }
                return result;
            }
            // 响应正常，但是请求整体处理失败，原因视具体的情况详细分析
            log.error("the whole image scan request failed. response:" + JSON
                    .toJSONString(scrResponse));
            return null;

        }
        // 响应异常
        log.error("响应异常：" + (httpResponse != null ? httpResponse.toString() + "状态码" + httpResponse
                .getStatus() : "服务端响应为null"));
        return null;
    }

    /**
     * 文字检测
     *
     * @param textList
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<CheckResult> checkText(List<TextTask> textList)
            throws UnsupportedEncodingException {
        TextScanRequest textScanRequest = new TextScanRequest();
        // 指定api返回格式
        textScanRequest.setAcceptFormat(FormatType.JSON);
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(MethodType.POST);
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(aliyunSdkBean.getRegionId());

        JSONObject data = new JSONObject();
        //检测场景
        data.put("scenes", Arrays.asList(aliyunGreenBean.getTextScenes()));
        data.put("tasks", textList);
        textScanRequest
                .setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), "UTF-8",
                        FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
        HttpResponse httpResponse = null;
        DefaultAcsClient client = getClient();
        try {
            httpResponse = client.doAction(textScanRequest);
        } catch (Exception e) {
            log.error("请求发送异常", e);
        }
        if (httpResponse != null && httpResponse.isSuccess()) {
            JSONObject scrResponse = JSON
                    .parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
            if (200 == scrResponse.getInteger("code")) {
                List<CheckResult> result = new LinkedList<>();
                JSONArray taskResults = scrResponse.getJSONArray("data");
                for (Object taskResult : taskResults) {
                    if (200 == ((JSONObject) taskResult).getInteger("code")) {
                        CheckResult checkResult = JSONObject
                                .parseObject(JSON.toJSONString(taskResult), CheckResult.class);
                        result.add(checkResult);
                        continue;
                    }
                    CheckResult checkResult = new CheckResult();
                    checkResult.setMsg("task process fail. task response:" + JSON
                            .toJSONString(taskResult));
                    checkResult.setCode(500);
                    result.add(checkResult);

                }
                return result;
            }
            // 响应正常，但是请求整体处理失败，原因视具体的情况详细分析
            log.error("detect not success. code:" + scrResponse.getInteger("code"));
            return null;
        }
        // 响应异常
        log.error(
                "响应异常：" + (httpResponse != null ? httpResponse.toString() + "状态码" + httpResponse
                        .getStatus() : "服务端响应为null"));
        return null;
    }

//    public static void main(String[] args) throws ClientException, UnsupportedEncodingException {
//        List<ImageTask> list = new LinkedList<>();
//        list.add(new ImageTask("https://www.baidu.com/img/bd_logo1.png"));
//        list.add(new ImageTask(
//                "https://img.alicdn.com/tfs/TB1CWhlmh6I8KJjy0FgXXXXzVXa-829-829.jpg"));
//        List<CheckResult> checkResults = Green.checkImage(list);
//        System.out.println(checkResults);
//
//        List<TextTask> textTaskList = new LinkedList<>();
//        textTaskList.add(new TextTask("耶路撒冷"));
//        textTaskList.add(new TextTask("vx 13121"));
//        List<CheckResult> textResult = Green.checkText(textTaskList);
//        System.out.println(textResult);
//    }
//

}
