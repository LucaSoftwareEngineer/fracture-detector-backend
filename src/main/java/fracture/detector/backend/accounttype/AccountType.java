package fracture.detector.backend.accounttype;

import fracture.detector.backend.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "account_type")
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_type_id")
    private Long idAccountType;

    @Column(name = "description", unique = true)
    private String description;

    @OneToMany(mappedBy = "accountType")
    private Set<User> users;

}
