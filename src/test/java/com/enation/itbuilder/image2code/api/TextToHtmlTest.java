package com.enation.itbuilder.image2code.api;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.output.TokenUsage;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class TextToHtmlTest {


    String apiKey = System.getenv("OPENAI_API_KEY");

    ChatLanguageModel model = OpenAiChatModel.builder()
            .apiKey(apiKey)// Please use your own OpenAI API key
            .modelName("gpt-4-turbo-preview")
            .timeout(Duration.ofMinutes(5))
            .maxTokens(4096)
            .temperature(0D)
//                .responseFormat("{ \"type\": \"json_object\" }")
            .build();


    @Test
    public void testAnalysis() throws Exception {
        AnalysisSetting analysisSetting = getAnalysisSetting();

        List<ChatMessage> chatMessages = TextToHtml.convertAnalyzeMessages(analysisSetting);
        call(chatMessages);
    }

    @NotNull
    private static AnalysisSetting getAnalysisSetting() {
        AnalysisSetting analysisSetting = new AnalysisSetting();
        analysisSetting.setIndustry("软件产品服务商");
        analysisSetting.setPageType("首页");
        analysisSetting.setBusiness("Javashop电商系统");
        analysisSetting.setFeatures("商城系统基于SpringBoot全端开源，更有微服务商城、商城中台、SAAS商城 实现。客户端基于uniapp开发，支持小程序商城 H5商城 APP商城 PC商城 。业务模式包含 B2B2C商城 S2B2C商城 O2O商城 B2B商城 多语言商城 跨境电商 分销商城 积分商城");
        analysisSetting.setCodeType("vue_tailwind");

        return analysisSetting;
    }

    @Test
    public void test() throws Exception {

        AnalysisSetting analysisSetting = getAnalysisSetting();

        String analysisResult = FileUtils.readFile("/response/analysis-result.txt");
        WorkFlowContext.setResult(ActionType.analysis, analysisResult);

        List<ChatMessage> chatMessages = TextToHtml.convertCreateMessage(analysisSetting, analysisResult);

        call(chatMessages);

    }

    private void call(List<ChatMessage> chatMessages) {
        System.out.println("sysPrompt : " + chatMessages.get(0));
        System.out.println("userMessage : " + chatMessages.get(1));
        System.out.println("=============================================");

        Response<AiMessage> response = model.generate(chatMessages);
        TokenUsage tokenUsage = response.tokenUsage();
        Integer total = tokenUsage.totalTokenCount();

        System.out.println("total : " + total);
        String answer = response.content().text();

        System.out.println(answer);
    }


}
