package com.enation.itbuilder.image2code.api;

import lombok.Data;

/**
 * 页面基本设定
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/

@Data
public class PageSetting {

    // 行业
    private String industry;
    // 页面类型
    private String pageType;
    // 板块规划
    private String sectionPlanning;
    // 是否自动规划板块
    private boolean autoPlanSection;
    // 颜色
    private String color;
    // 风格
    private String style;
    // 其他需求
    private String otherRequirements;

    private String codeType;

    //openai apikey
    private String apiKey;
}
