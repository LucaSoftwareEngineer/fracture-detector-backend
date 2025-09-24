package fracture.detector.backend.accounttype;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    public List<AccountTypeResponse> getAccountTypes() {
        List<AccountType> accountTypeList = accountTypeRepository.findAll();
        List<AccountTypeResponse> accountTypeResponseList = new ArrayList<>();

        accountTypeList.forEach(typeOfList -> {
            AccountTypeResponse typeOfResponse = new AccountTypeResponse();
            typeOfResponse.setId(typeOfList.getIdAccountType());
            typeOfResponse.setDescription(typeOfList.getDescription());
            accountTypeResponseList.add(typeOfResponse);
        });

        return accountTypeResponseList;
    }

    public AccountType findById(Long id) {
        return accountTypeRepository.findById(id).get();
    }

}
