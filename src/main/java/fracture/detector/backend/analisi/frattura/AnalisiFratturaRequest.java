package fracture.detector.backend.analisi.frattura;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnalisiFratturaRequest {

    private LocalDateTime dataAnalisi;
    private MultipartFile fileLastra;
    private Long userId;

}
