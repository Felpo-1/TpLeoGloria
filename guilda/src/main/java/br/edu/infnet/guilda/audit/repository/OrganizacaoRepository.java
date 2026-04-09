package br.edu.infnet.guilda.audit.repository;

import br.edu.infnet.guilda.audit.domain.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
}
