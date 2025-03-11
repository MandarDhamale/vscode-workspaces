package com.mandar.spring_web_template_integration.util.email;

import lombok.Data;

@Data
public class emailDetails {

    private String recipient;
    private String msgBody;
    private String subject;

}
