package fracture.detector.backend.accounttype;

import fracture.detector.backend.accounttype.AccountType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping("/list")
    public List<AccountTypeResponse> getAccountTypes() {
        return accountTypeService.getAccountTypes();
    }

}
