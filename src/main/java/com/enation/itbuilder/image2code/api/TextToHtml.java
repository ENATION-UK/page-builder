package com.enation.itbuilder.image2code.api;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.enation.itbuilder.image2code.api.FileUtils.readFile;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
public class TextToHtml {

    public static final DefaultResourceLoader DEFAULT_RESOURCE_LOADER = new DefaultResourceLoader();

    private static final String promptsLocation = "classpath:/prompts/by-text";

    /**
     * 组织分析需求的提示词
     * @param analysisSetting
     * @return
     */
    public static List<ChatMessage> convertAnalyzeMessages(AnalysisSetting analysisSetting) {
        Locale locale = LocaleContextHolder.getLocale();
        String languageDisplayName = locale.getDisplayLanguage(locale);

        String sysPrompt = readFile("/prompts/by-text/analysis.txt");
        sysPrompt+="\n\n # 注意：客户的语言是"+languageDisplayName;
        System.out.println(sysPrompt);
        String userPrompt = "一个${analysisSetting.industry}行业的${analysisSetting.pageType}，主营业务为${analysisSetting.business}，产品或服务的优势和特色为:${analysisSetting.features}";

        //用freemarker解析模版
        userPrompt = parsePrompt(userPrompt, analysisSetting,"analysisSetting");

        SystemMessage systemMessage = SystemMessage.from(sysPrompt);
        UserMessage userMessage = UserMessage.from(userPrompt );

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        return messageList;
    }

    public static List<ChatMessage> convertCreateMessage(AnalysisSetting analysisSetting,String analysisResult ) {
        String codeType = analysisSetting.getCodeType();
        Locale locale = LocaleContextHolder.getLocale();
        String languageDisplayName = locale.getDisplayLanguage(locale);


        codeType = "/prompts/by-text/"+codeType+".txt";

        String sysPrompt = readFile(codeType);
        sysPrompt+="\n\n # 注意：客户的语言是"+languageDisplayName;


        String userPrompt = "一个${analysisSetting.industry}行业的${analysisSetting.pageType}，主营业务为${analysisSetting.business}，产品或服务的优势和特色为:${analysisSetting.features}";
        //用freemarker解析模版
        userPrompt = parsePrompt(userPrompt, analysisSetting,"analysisSetting");

        userPrompt = userPrompt + "\n" + analysisResult;

        SystemMessage systemMessage = SystemMessage.from(sysPrompt);
        UserMessage userMessage = UserMessage.from(userPrompt );

        List<ChatMessage> messageList = new ArrayList<>();
        messageList.add(systemMessage);
        messageList.add(userMessage);

        return messageList;

    }


    public static String parsePrompt( String templateString,Object pageSetting,String modelName ) {
        // 配置Freemarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setWrapUncheckedExceptions(true);


        // 使用Template类的构造器直接从字符串创建模板
        Template template = null;
        try {
            template = new Template("name", new StringReader(templateString), cfg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 准备数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(modelName, pageSetting);

        // 合并模板和数据模型
        Writer out = new StringWriter();
        try {
            template.process(dataModel, out);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out.toString();
    }
}
