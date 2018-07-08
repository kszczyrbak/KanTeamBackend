package szczkrzy.kanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entity.KanTeamUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<KanTeamUser, Integer> {

    List<KanTeamUser> findAll();

    Optional<KanTeamUser> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KanTeamUser user);

    KanTeamUser findByEmail(String login);


}
