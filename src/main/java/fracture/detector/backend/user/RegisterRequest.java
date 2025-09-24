package fracture.detector.backend.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String name;
    private String surname;
    private String username;
    private String password;
    private int accountTypeSelected;

}
