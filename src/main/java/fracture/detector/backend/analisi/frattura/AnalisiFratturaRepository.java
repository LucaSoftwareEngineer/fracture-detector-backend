package fracture.detector.backend.analisi.frattura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fracture.detector.backend.user.User;

import java.util.List;

@Repository
public interface AnalisiFratturaRepository extends JpaRepository<AnalisiFrattura, Long> {

    public List<AnalisiFrattura> findByUser(User user);

}
