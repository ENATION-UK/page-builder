package com.enation.itbuilder.image2code.api;

import lombok.Data;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Data
public class GenerateParameter {

    //图片的base64编码
    private String base64String;

    //openai apikey
    private String apiKey;

    private String codeType;

}
