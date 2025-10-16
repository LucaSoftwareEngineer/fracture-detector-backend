package fracture.detector.backend.analisi.frattura;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnalisiFratturaListResponse {
    private int idAnalisiFrattura;
    private LocalDateTime dataAnalisi;
    private String nomeFileLastra;
    private Boolean esito;
}
