package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entities.KTColorMapping;

@Repository
public interface ColorMappingRepository extends JpaRepository<KTColorMapping, Integer> {

    void delete(KTColorMapping mapping);
}
