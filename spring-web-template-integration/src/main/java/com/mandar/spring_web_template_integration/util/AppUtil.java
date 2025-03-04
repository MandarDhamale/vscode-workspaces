package com.mandar.spring_web_template_integration.util;

import java.io.File;

public class AppUtil {
    

    public static String getUploadPath(String fileName){
        return new File("src/main/resources/static/uploads").getAbsolutePath() + "/" + fileName;
     }



}
