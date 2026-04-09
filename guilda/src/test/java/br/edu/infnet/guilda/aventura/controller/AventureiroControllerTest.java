package br.edu.infnet.guilda.aventura.controller;

import br.edu.infnet.guilda.aventura.domain.Aventureiro;
import br.edu.infnet.guilda.aventura.domain.ClasseAventureiro;
import br.edu.infnet.guilda.aventura.dto.AventureiroRequestDTO;
import br.edu.infnet.guilda.aventura.service.AventureiroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AventureiroController.class)
public class AventureiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AventureiroService aventureiroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCriarAventureiro() throws Exception {
        AventureiroRequestDTO request = new AventureiroRequestDTO();
        request.setNome("Arthur Pendragon");
        request.setClasse(ClasseAventureiro.GUERREIRO);
        request.setNivel(10);

        Aventureiro aventureiroMock = Aventureiro.builder()
                .id(1L)
                .nome("Arthur Pendragon")
                .classe(ClasseAventureiro.GUERREIRO)
                .nivel(10)
                .build();

        Mockito.when(aventureiroService.registrar(any(), any(), any())).thenReturn(aventureiroMock);

        mockMvc.perform(post("/aventureiros")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Org-Id", "1")
                .header("X-User-Id", "1")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Arthur Pendragon"));
    }

    @Test
    @WithMockUser
    public void testListarAventureiros() throws Exception {
        Aventureiro aventureiroMock = Aventureiro.builder()
                .id(1L)
                .nome("Arthur")
                .classe(ClasseAventureiro.GUERREIRO)
                .nivel(10)
                .build();

        PageImpl<Aventureiro> page = new PageImpl<>(List.of(aventureiroMock), PageRequest.of(0, 10), 1);

        Mockito.when(aventureiroService.listar(any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/aventureiros")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Arthur"));
    }
}
