package szczkrzy.kanteam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entity.KTUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<KTUser, Integer> {

    List<KTUser> findAll();

    Optional<KTUser> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KTUser user);

    KTUser findByEmail(String login);


}
