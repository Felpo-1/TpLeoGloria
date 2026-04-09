package br.edu.infnet.guilda.aventura.repository;

import br.edu.infnet.guilda.aventura.domain.LogAventura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAventuraRepository extends MongoRepository<LogAventura, String> {
}
