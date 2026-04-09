package br.edu.infnet.guilda.aventura.repository;

import br.edu.infnet.guilda.aventura.domain.ParticipacaoMissao;
import br.edu.infnet.guilda.aventura.domain.ParticipacaoMissaoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipacaoMissaoRepository extends JpaRepository<ParticipacaoMissao, ParticipacaoMissaoId> {
}
