package com.Aeb.AebDMS.config;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OcrConfig {

    @Bean
    public ITesseract tesseract() {
        Tesseract tess = new Tesseract();
        tess.setDatapath("tessdata");   // path to tessdata folder
        tess.setLanguage("fra");                   // adjust languages as needed
        return tess;
    }

}