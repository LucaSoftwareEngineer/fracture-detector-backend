package fracture.detector.backend.password.reset;

import fracture.detector.backend.config.MailConfig;
import fracture.detector.backend.exceptions.OtpIsNotvalid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import fracture.detector.backend.user.User;
import fracture.detector.backend.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${config.mail.address}")
    private String mailAddressConfig;

    public void reset(PasswordResetRequest request) {

        int otp = generateOtpCode();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime optExpiration = now.plusMinutes(60);

        User user = userRepository.findByUsername(request.getUsername()).get();

        if (user != null) {
            user.setOptResetPassword(String.valueOf(otp));
            user.setOptExpiration(optExpiration);
            userRepository.save(user);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailAddressConfig);
            message.setTo(request.getUsername());
            message.setSubject("Reset della password - Fracture Detector");
            message.setText(getMailReset(otp));
            javaMailSender.send(message);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    public PasswordUpdateResponse update(PasswordUpdateRequest request) throws OtpIsNotvalid {
        String username = request.getUsername();
        String rawPassword = request.getRawPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        String otp = request.getOptResetPassword();
        User user = userRepository.findByUsername(username).get();
        if (user != null) {
            if (!user.getOptResetPassword().equals(otp)) {
                throw new OtpIsNotvalid();
            } else {
                if (LocalDateTime.now().isBefore(user.getOptExpiration())) {
                    user.setPassword(hashedPassword);
                    user.setOptResetPassword(null);
                    user.setOptExpiration(null);
                    try {
                        userRepository.save(user);
                    } catch (Exception e) {
                        return new PasswordUpdateResponse(false);
                    }
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(mailAddressConfig);
                    message.setTo(request.getUsername());
                    message.setSubject("Modifica della password - Fracture Detector");
                    message.setText(getMailUpdate());
                    javaMailSender.send(message);
                    return new PasswordUpdateResponse(true);
                } else {
                    throw new OtpIsNotvalid();
                }
            }
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    public static String getMailReset(int otp) {
        String MAIL_TEMPLATE = new String();
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("Gentile cliente, \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("le confermiamo di aver ricevuto la sua richiesta di modifica della password \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("può modificarla recandoti al seguente link: \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("http://localost:4200/update/password \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("ed inserendo il seguente codice quando ti verrà richiesto \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("Codice: "+String.valueOf(otp)+" \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("cordiali saluti \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("Il Team di Fracture Detector \n");
        return MAIL_TEMPLATE;
    }

    public static String getMailUpdate() {
        String MAIL_TEMPLATE = new String();
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("Gentile cliente, \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("le confermiamo che la sua password è stata modificata \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("cordiali saluti \n");
        MAIL_TEMPLATE = MAIL_TEMPLATE.concat("Il Team di Fracture Detector \n");
        return MAIL_TEMPLATE;
    }

    public static int generateOtpCode() {
        Random random = new Random();
        int range = 90000000;
        return 10000000 + random.nextInt(range);
    }

}
