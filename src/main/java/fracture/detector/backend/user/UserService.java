package fracture.detector.backend.user;

import fracture.detector.backend.account.type.AccountTypeService;
import fracture.detector.backend.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AccountTypeService accountTypeService;

    public RegisterResponse registerUser(RegisterRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setUsername(request.getUsername());
        user.setPassword(hashedPassword);
        user.setAccountType(accountTypeService.findById(Long.valueOf(request.getAccountTypeSelected())));
        User userSave = userRepository.save(user);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUsername(user.getUsername());
        return registerResponse;
    }

    public UserDetailsResponse getUserDetails(String token) {
        token = token.substring(7);
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username).get();
        if (user == null) {
            new UsernameNotFoundException("utente non trovato");
        }
        UserDetailsResponse res = new UserDetailsResponse();
        res.setUsername(user.getUsername());
        res.setName(user.getName());
        res.setSurname(user.getSurname());
        res.setType(user.getAccountType().getDescription());
        res.setNumeroAnalisiConFrattura(0);
        res.setNumeroAnalisiSenzaFrattura(0);
        res.setPercentualeMediaAccuratezzaAnalisi(0);
        return res;
    }

    public Boolean tokenCheck(String token) {
        String username = jwtUtil.extractUsername(token);
        return jwtUtil.validateToken(token, username);
    }

}
