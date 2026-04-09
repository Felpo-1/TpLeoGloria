package br.edu.infnet.guilda.audit.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_entries", schema = "audit")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_user_id")
    private Usuario actorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_api_key_id")
    private ApiKey actorApiKey;

    @Column(nullable = false, length = 30)
    private String action;

    @Column(name = "entity_schema", nullable = false, length = 60)
    private String entitySchema;

    @Column(name = "entity_name", nullable = false, length = 80)
    private String entityName;

    @Column(name = "entity_id", length = 80)
    private String entityId;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private OffsetDateTime occurredAt;

    @Column(length = 255)
    private String userAgent;

    @Column(nullable = false)
    private Boolean success;
}
