#### pom.xml 引入
```
        <dependency>
            <groupId>com.msop</groupId>
            <artifactId>green</artifactId>
            <version>1.0.0</version>
        </dependency>
```
#### application.yml 配置
```
aliyun:
   sdk:
     enabled: true
     access-key-id: ****
     accessKeySecret: ****
     regionId: cn-shanghai
     green:
       image-scenes:
         - porn
         - terrorism
       text-scenes:
         - antispam
```
---
### 使用文档
#### 实例注入
``
    @Resource
    private GreenTemplate greenTemplate;
``
#### 图片检测
```
        List<ImageTask> list = new LinkedList<>();
        list.add(new ImageTask("https://www.baidu.com/img/bd_logo1.png"));
        list.add(new ImageTask("https://img.alicdn.com/tfs/TB1CWhlmh6I8KJjy0FgXXXXzVXa-829-829.jpg"));
        
        List<CheckResult> checkResults = greenTemplate.checkImage(list);
        System.out.println(checkResults);
        return checkResults;
```
#### 文本检测
```
        List<TextTask> list = new LinkedList<>();
        list.add(new TextTask("测试文字"));
        List<CheckResult> checkResults = greenTemplate.checkText(list);
        System.out.println(checkResults);
        return checkResults;
```