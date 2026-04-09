package br.edu.infnet.guilda.aventura.repository;

import br.edu.infnet.guilda.audit.domain.Organizacao;
import br.edu.infnet.guilda.audit.domain.Usuario;
import br.edu.infnet.guilda.audit.repository.OrganizacaoRepository;
import br.edu.infnet.guilda.audit.repository.UsuarioRepository;
import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import br.edu.infnet.guilda.aventura.domain.ClasseAventureiro;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class AventureiroRepositoryTest {

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void givenAventureiro_whenSave_thenReturnSavedAventureiro() {
        Organizacao org = organizacaoRepository.save(Organizacao.builder()
                .nome("Guilda Central")
                .ativo(true)
                .createdAt(java.time.OffsetDateTime.now())
                .build());
        Usuario user = usuarioRepository.save(Usuario.builder()
                .nome("Test User")
                .email("test@infnet.edu.br")
                .senhaHash("password")
                .status("ATIVO")
                .organizacao(org)
                .createdAt(java.time.OffsetDateTime.now())
                .updatedAt(java.time.OffsetDateTime.now())
                .build());

        Aventureiro aventureiro = Aventureiro.builder()
                .nome("Gandalf")
                .classe(ClasseAventureiro.MAGO)
                .nivel(20)
                .organizacao(org)
                .usuarioCadastro(user)
                .ativo(true)
                .build();

        Aventureiro saved = aventureiroRepository.save(aventureiro);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Gandalf", saved.getNome());

        Optional<Aventureiro> found = aventureiroRepository.findByIdWithCompanheiro(saved.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Gandalf", found.get().getNome());
    }
}
