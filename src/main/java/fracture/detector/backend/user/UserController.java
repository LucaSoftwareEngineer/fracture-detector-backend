package fracture.detector.backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok().body(userService.registerUser(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/token/check")
    public ResponseEntity<TokenCheckResponse> tokenCheck(@RequestBody TokenCheckRequest request) {
        if (userService.tokenCheck(request.getToken())) {
            return ResponseEntity.ok().body(new TokenCheckResponse(true));
        }
        return ResponseEntity.ok().body(new TokenCheckResponse(false));
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetailsResponse> getUserDetails(@RequestHeader(name = "Authorization") String token) {
        try {
            return ResponseEntity.ok().body(userService.getUserDetails(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
