package br.edu.infnet.guilda.aventura.service;

import br.edu.infnet.guilda.audit.domain.Organizacao;
import br.edu.infnet.guilda.audit.domain.Usuario;
import br.edu.infnet.guilda.audit.repository.OrganizacaoRepository;
import br.edu.infnet.guilda.audit.repository.UsuarioRepository;
import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import br.edu.infnet.guilda.aventura.domain.enums.ClasseAventureiro;
import br.edu.infnet.guilda.aventura.domain.Companheiro;
import br.edu.infnet.guilda.aventura.dto.AventureiroRequestDTO;
import br.edu.infnet.guilda.aventura.dto.CompanheiroRequestDTO;
import br.edu.infnet.guilda.aventura.repository.AventureiroRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AventureiroService {

    private final AventureiroRepository aventureiroRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LogAventuraService logAventuraService;

    @Transactional
    public Aventureiro registrar(AventureiroRequestDTO dto, Long orgId, Long userId) {
        Organizacao org = organizacaoRepository.findById(orgId)
            .orElseThrow(() -> new EntityNotFoundException("Organização legada não encontrada para ID " + orgId));
        Usuario user = usuarioRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para ID " + userId));

        Aventureiro aventureiro = Aventureiro.builder()
            .nome(dto.getNome())
            .classe(dto.getClasse())
            .nivel(dto.getNivel())
            .organizacao(org)
            .usuarioCadastro(user)
            .ativo(true)
            .build();

        Aventureiro saved = aventureiroRepository.save(aventureiro);
        logAventuraService.registrarLog("REGISTRO_AVENTUREIRO", "Aventureiro salvo: " + saved.getNome(), userId);
        return saved;
    }

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "aventureiros", key = "#pageable.pageNumber")
    public Page<Aventureiro> listar(ClasseAventureiro classe, Boolean ativo, Integer nivelMin, Pageable pageable) {
        Specification<Aventureiro> spec = Specification.where(null);
        if (classe != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("classe"), classe));
        }
        if (ativo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ativo"), ativo));
        }
        if (nivelMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("nivel"), nivelMin));
        }
        return aventureiroRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "aventureiro_detalhe", key = "#id")
    public Aventureiro consultarPorId(Long id) {
        return aventureiroRepository.findByIdWithCompanheiro(id)
            .orElseThrow(() -> new EntityNotFoundException("Aventureiro não encontrado"));
    }

    @Transactional
    public Aventureiro atualizarDado(Long id, AventureiroRequestDTO dto) {
        Aventureiro aventureiro = aventureiroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Aventureiro não encontrado"));
        
        aventureiro.setNome(dto.getNome());
        aventureiro.setClasse(dto.getClasse());
        aventureiro.setNivel(dto.getNivel());
        
        logAventuraService.registrarLog("ATUALIZACAO_AVENTUREIRO", "Aventureiro atualizado: " + id, null);
        return aventureiroRepository.save(aventureiro);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"aventureiros", "aventureiro_detalhe"}, allEntries = true)
    public Aventureiro inativar(Long id) {
        Aventureiro aventureiro = aventureiroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Aventureiro não encontrado"));
        aventureiro.setAtivo(false);
        return aventureiroRepository.save(aventureiro);
    }

    @Transactional
    public Aventureiro ativar(Long id) {
        Aventureiro aventureiro = aventureiroRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Aventureiro não encontrado"));
        aventureiro.setAtivo(true);
        return aventureiroRepository.save(aventureiro);
    }

    @Transactional
    public Aventureiro atualizarCompanheiro(Long id, CompanheiroRequestDTO dto) {
        Aventureiro aventureiro = aventureiroRepository.findByIdWithCompanheiro(id)
            .orElseThrow(() -> new EntityNotFoundException("Aventureiro não encontrado"));

        if (aventureiro.getCompanheiro() == null) {
            Companheiro comp = Companheiro.builder()
                .aventureiro(aventureiro)
                .nome(dto.getNome())
                .especie(dto.getEspecie())
                .indiceLealdade(dto.getLealdade())
                .build();
            aventureiro.setCompanheiro(comp);
        } else {
            aventureiro.getCompanheiro().setNome(dto.getNome());
            aventureiro.getCompanheiro().setEspecie(dto.getEspecie());
            aventureiro.getCompanheiro().setIndiceLealdade(dto.getLealdade());
        }

        return aventureiroRepository.save(aventureiro);
    }

    @Transactional
    public void removerCompanheiro(Long id) {
        Aventureiro aventureiro = aventureiroRepository.findByIdWithCompanheiro(id)
            .orElseThrow(() -> new EntityNotFoundException("Aventureiro não encontrado"));
        
        if (aventureiro.getCompanheiro() != null) {
            aventureiro.getCompanheiro().setAventureiro(null);
            aventureiro.setCompanheiro(null);
            aventureiroRepository.save(aventureiro);
        }
    }
}
