package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entity.KTColumn;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColumnRepository extends JpaRepository<KTColumn, Integer> {

    List<KTColumn> findAll();

    Optional<KTColumn> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KTColumn column);

}
