package fracture.detector.backend.password.reset;

import fracture.detector.backend.exceptions.OtpIsNotvalid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/reset")
    public void reset(@RequestBody PasswordResetRequest request) {
        passwordResetService.reset(request);
    }

    @PostMapping("/update")
    public ResponseEntity<PasswordUpdateResponse> update(@RequestBody PasswordUpdateRequest request) {
        try {
            return ResponseEntity.ok().body(passwordResetService.update(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
