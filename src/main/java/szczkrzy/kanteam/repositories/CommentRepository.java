package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entity.KTComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<KTComment, Integer> {

    List<KTComment> findAll();

    Optional<KTComment> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KTComment comment);

}
