package fracture.detector.backend.analisi.frattura;

import fracture.detector.backend.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "analisi_frattura")
public class AnalisiFrattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_analisi_frattura")
    private int idAnalisiFrattura;

    @Column(name = "data_analisi")
    private LocalDateTime dataAnalisi;

    @Column(name = "nome_file_lastra")
    private String nomeFileLastra;

    @Column(name = "esito")
    private Boolean esito;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
