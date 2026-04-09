package br.edu.infnet.guilda.aventura.repository;

import br.edu.infnet.guilda.aventura.domain.Missao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long>, JpaSpecificationExecutor<Missao> {
}
