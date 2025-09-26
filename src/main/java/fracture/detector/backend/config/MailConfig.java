package fracture.detector.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender; // Importa l'interfaccia corretta
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${config.mail.smtp}")
    private String mailSmtpConfig;

    @Value("${config.mail.port}")
    private int mailPortConfig;

    @Value("${config.mail.address}")
    private String mailAddressConfig;

    @Value("${config.mail.token}")
    private String mailTokenConfig;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailSmtpConfig);
        mailSender.setPort(mailPortConfig);
        mailSender.setUsername(mailAddressConfig);
        mailSender.setPassword(mailTokenConfig);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
