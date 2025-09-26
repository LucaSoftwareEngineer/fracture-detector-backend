package fracture.detector.backend.user;

import fracture.detector.backend.accounttype.AccountType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Users")
@ToString
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long idUser;

  @Column(name="name")
  private String name;

  @Column(name="surname")
  private String surname;

  @Column(name="username", unique = true)
  private String username;

  @Column(name="password")
  private String password;

  @Column(name="opt_reset_password")
  private String optResetPassword;

  @Column(name="opt_expiration")
  private LocalDateTime optExpiration;

  @ManyToOne
  @JoinColumn(name = "account_type_id", nullable = false)
  private AccountType accountType;

}
