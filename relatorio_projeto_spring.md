# Relatório do Projeto de Bloco - Spring Boot API (Registro Oficial da Guilda)

## 1. Inicialização e Criação do Projeto
Para basear nossa ferramenta, utilizei como ponto de partida a ferramenta de geração **Spring Initializr via Interface Web** no site [start.spring.io](https://start.spring.io/).
- **Gerenciador de Dependência:** Optei por usar o **Maven** em detrimento ao Gradle. O Maven me forneceu uma estrutura de `pom.xml` mais familiar, onde eu apliquei a **autoconfiguração do Spring Boot** sem lidar direto com scripts groovy/kotlin.
- **IDE:** Utilizei o VSCode e IntelliJ configurados com o Plugin *Spring Boot Extension Pack*, o que permitiu visualizar os beans gerados e executar os testes de ponta a ponta sem dor de cabeça. O ambiente do VSCode possibilita o Hot Reload.
- O CLI do Spring Boot também faria sentido para prototipagens rápidas com scripts groovy isolados, porém para um sistema multicamadas corporativo de persistência como o nosso, a geração web inicializa as pastas do padrão MVC de forma imensamente mais sólida com Maven!

---

## 2. Visão de APIs REST
No pacote de `controller`, expomos nossa classe `AventureiroController`. Adotamos a anotação padrão `@RequestMapping("/aventureiros")` e subdividimos nossas rotas.

A aplicação evita qualquer repetição lógica usando o ecossistema de *Services* invocados no *Controller*:
- `POST /aventureiros`: Criação (retorna 201 Created).
- `GET /aventureiros`: Recuperação com paginação e suporte a Search Param/Especificações.
- `GET /aventureiros/{id}`: Recuperar detalhe do aventureiro junto com seus sub-objetos (ex: Companheiros).
- `PUT /aventureiros/{id}`: Atualiza os dados principais sob a restrição do RequestDTO sem violar dados de base.
- `PATCH /aventureiros/{id}/ativar` e `inativar`: Operações coesas de desabilitação sistêmica no lugar de `DELETE` físico.

> Diferenciamos os cenários em caso de sucesso (código `20x`), recursos inexistentes (`404` NotFoundException via ControllerAdvice no projeto), violação e dados inválidos (DataException e `@Valid`).

### Exemplos do Endpoint via Postman:
```json
// POST /aventureiros 
// Header: X-Org-Id: 1 | X-User-Id: 1
{
  "nome": "Aragorn",
  "classe": "GUERREIRO",
  "nivel": 25
}
```
**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "Aragorn",
  "dataCriacao": "2024-04-09T12:00:00"
}
```

---

## 3. Persistência Polyglota e Autoconfiguração Dinâmica
- **JPA (H2 + Postgres):** Mapeamos `@Entity Aventureiro` de onde utilizamos o banco de dados oficial (definido por driver). Abstração pela interface Spring `AventureiroRepository extends JpaRepository`, garantindo *Pagination/Sort*.
- **Cache de Performance via Redis:** Usamos a propriedade de `@EnableCaching` e anotamos os métodos `listar()` da nossa Service Layer. Por debaixo dos panos, nossa API passa a salvar em Key/Value no Redis e recuperar de modo imensamente rápido, invalidando através de `@CacheEvict` no update. O Spring nos isentou do código de Boilerplate com suas autoconfigurações baseadas em `Environment properties`.
- **Relatório via Documento (NoSQL):** Construímos a Entidade `@Document LogAventura` para o backend MongoDB, salvando os logs de registro não-relacionais, que suportariam enormes matrizes mutáveis. Usamos a interface base `extend MongoRepository`.

---

## 4. Testes e Validação Total
A pirâmide de testes nos dá clareza que está tudo certo. Nossa suíte abrange:
- `@WebMvcTest(AventureiroController.class)` com mocks parciais isolados no *Unit Layer*.
- `@DataJpaTest` com inicialização apenas do slice nativo JPA rodando sob um banco volátil customizado *H2 Database*.
- `@SpringBootTest` emulando todo o container Web + Mongo + Cache habilitados em Integration Layer.

---

## 5. Medidas de Segurança
Implementamos a dependência `spring-boot-starter-security`.
Invocamos a chain `SecurityFilterChain` e validamos com as propriedades nativas o Auth básico, mantendo os logs de diagnostico abertos unicamente pro admin/user via configuração manual de properties `app.security.admin`. Isso impediu os vazamentos da rota de aventureiros se não contiver Auth Bearer/Basic válido.

---

## 6. Realização de Deploy (Empacotamento, Dockerização)
Por fim preparamos o ambiente corporativo com `.jar` via Maven `spring-boot-maven-plugin`.
1. Modificamos os `.yml/properties` para exportar dados do **Actuator** (adotando endpoint universal de infraestrutra de health checks `/actuator/health`).
2. Criamos o nosso `Dockerfile` (Multistage builder base `eclipse-temurin:17-jre`), embutindo nosso executável isolado.
3. Adotamos o `docker-compose.yml` final, instanciando os clusters: api-guilda vinculada com nossos containers de "postgres", "redis" e "mongodb", validando na checagem health se o banco está de pé previamente.
