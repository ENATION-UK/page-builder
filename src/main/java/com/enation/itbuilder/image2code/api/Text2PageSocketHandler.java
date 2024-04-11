package com.enation.itbuilder.image2code.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文本转页面 socket handler
 */
public class Text2PageSocketHandler extends TextWebSocketHandler {

    private static ObjectMapper mapper = new ObjectMapper();

    private static final Map<String, String> CODE_TYPES = Stream.of(CodeType.values())
            .collect(Collectors.toMap(CodeType::toString, CodeType::getDisplayText));

    private String apiAddress;


    public Text2PageSocketHandler(String apiAddress) {
        this.apiAddress = apiAddress;
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


        AnalysisSetting generateParameter = mapper.readValue(payload, AnalysisSetting.class);

        sse(session, generateParameter);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }


    public void sse(WebSocketSession session, AnalysisSetting generateParameter) throws Exception {

        OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder();

        builder.apiKey(generateParameter.getApiKey())// Please use your own OpenAI API key
                .modelName("gpt-4-0125-preview")
                .timeout(Duration.ofMinutes(5))
                .maxTokens(4096)
                .temperature(0.5D);

        if (StringUtils.hasText(apiAddress)) {
            builder.baseUrl(apiAddress);
        }




        ActionType actionType = ActionType.valueOf(generateParameter.getActionType());
        List<ChatMessage> messageList = null;
        if (actionType.equals(ActionType.analysis)) {
            messageList=TextToHtml.convertAnalyzeMessages(generateParameter);
        }

        if (actionType.equals(ActionType.create)) {
            //页面生成要非常严谨
            builder.temperature(0.5);
            messageList=TextToHtml.convertCreateMessage(generateParameter,generateParameter.getAnalysisResult());
        }

        PageHtmlResponseHandler pageHtmlResponseHandler = new PageHtmlResponseHandler(session,actionType);

        StreamingChatLanguageModel streamModel = builder.build();

        streamModel.generate(messageList,pageHtmlResponseHandler);


        System.out.println("api req");
    }



}

