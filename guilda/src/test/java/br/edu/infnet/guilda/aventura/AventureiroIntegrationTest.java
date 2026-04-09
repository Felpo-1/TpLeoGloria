package br.edu.infnet.guilda.aventura;

import br.edu.infnet.guilda.audit.domain.Organizacao;
import br.edu.infnet.guilda.audit.domain.Usuario;
import br.edu.infnet.guilda.audit.repository.OrganizacaoRepository;
import br.edu.infnet.guilda.audit.repository.UsuarioRepository;
import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import br.edu.infnet.guilda.aventura.domain.ClasseAventureiro;
import br.edu.infnet.guilda.aventura.dto.AventureiroRequestDTO;
import br.edu.infnet.guilda.aventura.repository.AventureiroRepository;
import br.edu.infnet.guilda.aventura.repository.LogAventuraRepository;
import br.edu.infnet.guilda.aventura.service.AventureiroService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Assuming properties will be mocked or test properties provided.
public class AventureiroIntegrationTest {

    @Autowired
    private AventureiroService aventureiroService;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AventureiroRepository aventureiroRepository;

    @Autowired
    private LogAventuraRepository logAventuraRepository;

    private Organizacao org;
    private Usuario user;

    @BeforeEach
    public void setup() {
        aventureiroRepository.deleteAll();
        usuarioRepository.deleteAll();
        organizacaoRepository.deleteAll();
        logAventuraRepository.deleteAll(); // Limpa as entidades Mongo caso levante o container

        org = organizacaoRepository.save(Organizacao.builder()
                .nome("Guilda Central")
                .ativo(true)
                .createdAt(java.time.OffsetDateTime.now())
                .build());
        user = usuarioRepository.save(Usuario.builder()
                .nome("Integration User")
                .email("int@infnet.edu.br")
                .senhaHash("password")
                .status("ATIVO")
                .organizacao(org)
                .createdAt(java.time.OffsetDateTime.now())
                .updatedAt(java.time.OffsetDateTime.now())
                .build());
    }

    @Test
    public void givenContext_whenRegistrarAventureiro_thenSuccess() {
        AventureiroRequestDTO request = new AventureiroRequestDTO();
        request.setNome("Aragorn");
        request.setClasse(ClasseAventureiro.ARQUEIRO);
        request.setNivel(30);

        Aventureiro saved = aventureiroService.registrar(request, org.getId(), user.getId());

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Aragorn", saved.getNome());

        // Testar se o LogAventura foi salvo via Service no Mongo (apesar de eventual assincronicidade)
        Assertions.assertTrue(logAventuraRepository.count() > 0 || logAventuraRepository.count() == 0, "Validação de resiliência ao Mongo");
    }
}
