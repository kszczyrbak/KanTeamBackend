package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import szczkrzy.kanteam.model.entities.KTSubtask;

public interface SubtaskRepository extends JpaRepository<KTSubtask, Integer> {
}
