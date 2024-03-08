# image to code

## 介绍
将图片转换为代码（HTML/Tailwind CSS、React、Bootstrap 或 Vue）。
它使用 GPT-4 Vision 生成代码

## 使用
### 在线体验

[http://image2code.itbuilder.cn:7008/](http://image2code.itbuilder.cn:7008/)

### 本地运行
```shell
mvn install
java -jar target/image-to-code-1.0-SNAPSHOT.jar
```
访问
```
http://localhost:8080
```
### 设置openai api key

![setting.png](setting.png)

点击设置，设置openai api key

### 上传图片
![upload.png](upload.png)
上传图片后gpt的Vision会识别图像并生成代码