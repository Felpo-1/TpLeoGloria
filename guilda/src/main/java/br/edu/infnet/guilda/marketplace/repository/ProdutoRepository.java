package br.edu.infnet.guilda.marketplace.repository;

import br.edu.infnet.guilda.marketplace.domain.Produto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends ElasticsearchRepository<Produto, String> {
}
