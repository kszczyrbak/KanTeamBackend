package szczkrzy.kanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entity.KTTeam;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<KTTeam, Integer> {

    List<KTTeam> findAll();

    Optional<KTTeam> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KTTeam team);

}
