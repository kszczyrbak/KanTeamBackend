package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entities.KTTask;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<KTTask, Integer> {

    List<KTTask> findAll();

    Optional<KTTask> findById(Integer integer);

    long count();

    void deleteById(Integer integer);

    void delete(KTTask task);

}
