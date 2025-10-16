package fracture.detector.backend.user;

import fracture.detector.backend.account.type.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private String type;
    private Long numeroAnalisiTotali;
    private Long numeroAnalisiSenzaFrattura;
    private Long numeroAnalisiConFrattura;
    private Long[] conteggiAnalisi;

}
