package szczkrzy.kanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import szczkrzy.kanteam.model.entity.KanTeamUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<KanTeamUser, Integer> {

    List<KanTeamUser> findAll();

    Optional<KanTeamUser> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KanTeamUser kanTeamUser);
}
