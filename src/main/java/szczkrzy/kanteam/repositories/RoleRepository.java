package szczkrzy.kanteam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szczkrzy.kanteam.model.entities.KTRole;

@Repository
public interface RoleRepository extends JpaRepository<KTRole, Integer> {

    KTRole findByName(String name);

}