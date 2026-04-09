package br.edu.infnet.guilda.audit.repository;

import br.edu.infnet.guilda.audit.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
