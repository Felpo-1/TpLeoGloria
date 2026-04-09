package br.edu.infnet.guilda.aventura.repository;

import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AventureiroRepository extends JpaRepository<Aventureiro, Long>, JpaSpecificationExecutor<Aventureiro> {
    
    @Query("SELECT a FROM Aventureiro a LEFT JOIN FETCH a.companheiro WHERE a.id = :id")
    Optional<Aventureiro> findByIdWithCompanheiro(Long id);
    
    Page<Aventureiro> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    
}
