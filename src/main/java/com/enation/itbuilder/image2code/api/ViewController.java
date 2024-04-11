package com.enation.itbuilder.image2code.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Controller
public class ViewController {

    @RequestMapping(value ="/text2page")
    public String text2page(){
        return "text2page";
    }

    @RequestMapping(value ="/image2page")
    public String image2page(){
        return "image2page";
    }
}
