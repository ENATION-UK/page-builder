package com.enation.itbuilder.image2code.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 1.0.0
 **/
@Slf4j
public class OpenAISourceListener extends EventSourceListener {

    private WebSocketSession session;

    public OpenAISourceListener( WebSocketSession session) {
       this.session = session;
    }

    private static ObjectMapper jsonObjMapper = new ObjectMapper();

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        // 连接打开时调用
        log.info("open");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if ("[DONE]".equals(data)) {
            return;
        }

        JsonNode jsonNode = null;
        try {
            jsonNode = jsonObjMapper.readTree(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode contentNode = jsonNode.path("choices").path(0)
                .path("delta").path("content");

        JsonNode finishReasonNode = jsonNode.path("choices").path(0)
                .path("finish_reason");

        String content = contentNode.asText();

        try {
            session.sendMessage(new TextMessage(content));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("closed");
        // 连接关闭时调用
    }

    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        // 连接失败时调用
        log.error("onFailure", t);
    }




}
