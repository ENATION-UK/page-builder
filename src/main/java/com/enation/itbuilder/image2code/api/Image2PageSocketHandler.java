package com.enation.itbuilder.image2code.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Image2PageSocketHandler extends TextWebSocketHandler {

    public static final DefaultResourceLoader DEFAULT_RESOURCE_LOADER = new DefaultResourceLoader();
    private static ObjectMapper mapper = new ObjectMapper();

    private static final Map<String, String> CODE_TYPES = Stream.of(CodeType.values())
            .collect(Collectors.toMap(CodeType::toString, CodeType::getDisplayText));

    private String apiAddress;

    private final String promptsLocation;

    public Image2PageSocketHandler(String apiAddress) {
        this(apiAddress, "classpath:/prompts/");
    }

    public Image2PageSocketHandler(String apiAddress, String promptsLocation) {
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

        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder();

        builder.apiKey(generateParameter.getApiKey())// Please use your own OpenAI API key
                .modelName("gpt-4-vision-preview")
                .timeout(Duration.ofMinutes(5))
                .maxTokens(4096)
                .temperature(0.0D);

        if (StringUtils.hasText(apiAddress)) {
            builder.baseUrl(apiAddress);
        }


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


        SystemMessage systemMessage = SystemMessage.from(prompt);

        UserMessage userMessage = UserMessage.from(
                TextContent.from("Please generate code based on the image"),
                ImageContent.from(generateParameter.getBase64String())
        );



        StreamingChatLanguageModel streamModel = builder.build();

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);


        ActionType actionType = ActionType.create;
        PageHtmlResponseHandler pageHtmlResponseHandler = new PageHtmlResponseHandler(session,actionType);

        streamModel.generate(messageList,pageHtmlResponseHandler);


        System.out.println("api req");
    }

    public String readPrompt(CodeType codeType) throws IOException {
        return DEFAULT_RESOURCE_LOADER
                .getResource(promptsLocation + codeType.toString() + ".txt")
                .getContentAsString(StandardCharsets.UTF_8);
    }
}

