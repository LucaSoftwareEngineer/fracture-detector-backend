package fracture.detector.backend.user;

import fracture.detector.backend.account.type.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

    private Long id;
    private String username;
    private AccountType accountType;

}
