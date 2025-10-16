package fracture.detector.backend.analisi.frattura;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import fracture.detector.backend.user.User;

import java.util.List;

@Repository
public interface AnalisiFratturaRepository extends JpaRepository<AnalisiFrattura, Long> {

    public List<AnalisiFrattura> findByUser(User user);

    @Query("select count(idAnalisiFrattura) as conteggio from AnalisiFrattura where user = :user")
    public Long contaNumeroAnalisiByUserId(User user);

    @Query("select count(idAnalisiFrattura) as conteggio from AnalisiFrattura where user = :user and esito = True")
    public Long contaNumeroAnalisiConFratturaByUserId(User user);

    @Query("select count(idAnalisiFrattura) as conteggio from AnalisiFrattura where user = :user and esito = False")
    public Long contaNumeroAnalisiSenzaFratturaByUserId(User user);

    @Query(
            " select count(a) " +
                    " from AnalisiFrattura a where a.user = :user " +
                    " and FUNCTION('MONTH', a.dataAnalisi) = :mese " +
                    " and FUNCTION('YEAR', a.dataAnalisi) = :annoCorrente ")
    public Long contaAnalisiMeseDiAnnoCorrente(
            @Param("user") User user,
            @Param("mese") int mese,
            @Param("annoCorrente") int annoCorrente);
}
