INSERT INTO audit.organizacoes (id, nome, ativo, created_at) VALUES (1, 'Guilda Oficial', true, CURRENT_TIMESTAMP) ON CONFLICT (id) DO NOTHING;
INSERT INTO audit.usuarios (id, organizacao_id, nome, email, senha_hash, status, created_at, updated_at) VALUES (1, 1, 'Admin Guilda', 'admin@guilda.com', 'hashed_pass_here', 'ATIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT (id) DO NOTHING;
