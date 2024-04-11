package com.enation.itbuilder.image2code.api;

import lombok.Data;

/**
 * 分析参数
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class AnalysisSetting {
    // 行业
    private String industry;

    // 页面类型
    private String pageType;

    // 业务
    private String business;

    // 功能特色，亮点、优势等
    private String features;

    private String codeType;

    private String actionType;

    //openai apikey
    private String apiKey;

    private String analysisResult;

}
