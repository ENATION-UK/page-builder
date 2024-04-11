package com.enation.itbuilder.image2code.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class PageHtmlResponseHandler implements StreamingResponseHandler {

    private static ObjectMapper jsonObjMapper = new ObjectMapper();
    private ActionType actionType;
    private WebSocketSession session;
    public PageHtmlResponseHandler(WebSocketSession session,ActionType actionType) {
        this.actionType=actionType;
        this.session = session;
    }
    @Override
    public void onNext(String data) {
//        System.out.println(data);
        try {
            WsResponse success = WsResponse.builder().type(this.actionType.name()).status("success").body(data).build();
            String jsonString = jsonObjMapper.writeValueAsString(success);

            session.sendMessage(new TextMessage(jsonString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onComplete(Response response) {


        try {
            WsResponse complete = WsResponse.builder().type(this.actionType.name()).status("complete").body("success").build();
            String jsonString = jsonObjMapper.writeValueAsString(complete);

            session.sendMessage(new TextMessage(jsonString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        TokenUsage tokenUsage = response.tokenUsage();
        Integer total = tokenUsage.totalTokenCount();
        System.out.println("------花费-----");
        System.out.println(total);
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
