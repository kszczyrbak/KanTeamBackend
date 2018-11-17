package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entities.KTBoard;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<KTBoard, Integer> {

    List<KTBoard> findAll();

    Optional<KTBoard> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KTBoard board);

}
