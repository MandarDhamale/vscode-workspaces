package com.mandar.spring_web_template_integration.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppConfig {

    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String mailTransportProtocol;

    @Value("${spring.mail.host}")
    private String springMailHost;

    @Value("${spring.mail.port}")
    private int springMailPort;

    @Value("${spring.mail.username}")
    private String springMailUsername;

    @Value("${spring.mail.password}")
    private String springMailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.ssl.trust}")
    private String smtpSslTrust;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(springMailHost);
        mailSender.setPort(springMailPort);

        mailSender.setUsername(springMailUsername);
        mailSender.setPassword(springMailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

}
