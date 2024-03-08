package com.enation.itbuilder.image2code.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyWebSocketHandler extends TextWebSocketHandler {

    public static final DefaultResourceLoader DEFAULT_RESOURCE_LOADER = new DefaultResourceLoader();
    private static ObjectMapper mapper = new ObjectMapper();

    private static final Map<String, String> CODE_TYPES = Stream.of(CodeType.values())
            .collect(Collectors.toMap(CodeType::toString, CodeType::getDisplayText));

    private String apiAddress;

    private final String promptsLocation;

    public MyWebSocketHandler(String apiAddress) {
        this(apiAddress, "classpath:/prompts/");
    }

    public MyWebSocketHandler(String apiAddress, String promptsLocation) {
        this.apiAddress = apiAddress;
        this.promptsLocation = promptsLocation;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.setBinaryMessageSizeLimit(10 * 1024 * 1024);
        session.setTextMessageSizeLimit(10 * 1024 * 1024);

        session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of(
                "type", "initCodeTypes",
                "data", CODE_TYPES
        ))));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理接收到的消息
        // 这里可以根据需要处理消息
        String payload = message.getPayload();


        GenerateParameter generateParameter = mapper.readValue(payload, GenerateParameter.class);

        sse(session, generateParameter);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }


    public void sse(WebSocketSession session, GenerateParameter generateParameter) throws Exception {
        OkHttpClient client = new OkHttpClient
                .Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .build();
        Map<String, Object> mainMap = new HashMap<>();

        mainMap.put("model", "gpt-4-vision-preview");

        // 利用枚举验证codeType，因为会作为文件名，尽量避免被恶意攻击
        CodeType codeType;
        try {
            codeType = CodeType.valueOf(generateParameter.getCodeType());
        } catch (IllegalArgumentException e) {
            session.sendMessage(new TextMessage("""
                    { "success": false, "body": "Invalid code type" }
                    """));
            return;
        }

        //读取prompt文件，若读取失败给出友好提示
        String prompt = null;
        try {
            prompt = readPrompt(codeType);
        } catch (IOException e) {
            session.sendMessage(new TextMessage("""
                    { "success": false, "body": "Sorry that code type doesn't work right now. Please try another one." }
                    """));
            return;
        }

        List<Map<String, Object>> messagesList = new ArrayList<>();
        Map<String, Object> message1 = new HashMap<>();
        message1.put("role", "system");
        message1.put("content", prompt);
        messagesList.add(message1);

        Map<String, Object> message2 = new HashMap<>();
        message2.put("role", "user");
        List<Map<String, Object>> contentList = new ArrayList<>();
        Map<String, Object> content1 = new HashMap<>();
        content1.put("type", "text");
        content1.put("text", "Please generate code based on the image");
        contentList.add(content1);

        Map<String, Object> content2 = new HashMap<>();
        Map<String, String> imageUrlMap = new HashMap<>();
        imageUrlMap.put("url", generateParameter.getBase64String());
        content2.put("type", "image_url");
        content2.put("image_url", imageUrlMap);
        contentList.add(content2);

        message2.put("content", contentList);
        messagesList.add(message2);

        mainMap.put("messages", messagesList);

        mainMap.put("max_tokens", 4096);
        mainMap.put("temperature", 0);
        mainMap.put("stream", true);

        Gson gson = new Gson();
        String json1 = gson.toJson(mainMap);

        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");

        Request request = new Request.Builder()
                .url(apiAddress)
                .addHeader("Authorization", "Bearer " + generateParameter.getApiKey())
                .addHeader("Content-Type", mediaType.toString())
                .addHeader("Cache-Control", "no-cache")
                .post(RequestBody.create(mediaType, json1))
                .build();

        EventSource.Factory factory = EventSources.createFactory(client);
        EventSource eventSource = factory.newEventSource(request, new OpenAISourceListener(session));

        System.out.println("api req");
    }

    public String readPrompt(CodeType codeType) throws IOException {
        return DEFAULT_RESOURCE_LOADER
                .getResource(promptsLocation + codeType.toString() + ".txt")
                .getContentAsString(StandardCharsets.UTF_8);
    }
}

