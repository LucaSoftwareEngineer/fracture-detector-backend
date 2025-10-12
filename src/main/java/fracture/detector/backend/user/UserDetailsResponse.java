package fracture.detector.backend.user;

import fracture.detector.backend.account.type.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

    private String username;
    private String name;
    private String surname;
    private String type;
    private int numeroAnalisiConFrattura;
    private int numeroAnalisiSenzaFrattura;
    private int percentualeMediaAccuratezzaAnalisi;

}
