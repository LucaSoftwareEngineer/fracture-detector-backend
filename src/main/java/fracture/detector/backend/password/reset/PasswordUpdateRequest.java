package fracture.detector.backend.password.reset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequest {

    private String username;
    private String optResetPassword;
    private String rawPassword;

}
