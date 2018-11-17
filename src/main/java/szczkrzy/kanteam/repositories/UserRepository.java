package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entities.KTUser;

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

    @Query("select u from KTUser u where LOWER(u.fullName) like LOWER(concat(?1, '%'))")
    List<KTUser> findByFullName(String name);


}
