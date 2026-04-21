USE bd_projeto;

INSERT INTO usuario(nome, email, senha, telefone, is_ativo, data_cadastro, possui_veiculo)
VALUES ('Adriano', 'adriano@gmail.com', 'a4Gj@j21', '00 00000-0000', TRUE, NOW(), true),
       ('Beto', 'beto@gmail.com', 'dWu!87y8', '00 00000-0001', TRUE, NOW(), true),
       ('Carla', 'carla@gmail.com', 'ac@g78Er', '00 00000-0002', TRUE, NOW(), true);

INSERT INTO veiculo(modelo, marca, ano, placa, cor, tipo, km_atual, id_usuario, is_ativo, data_ultima_atualizacao_km)
VALUES ('Onix Plus', 'Chevrolet', 2022, 'BTA2E19', 'Prata', 'Carro', 45200, 1, TRUE, NOW()),
       ('Cronos', 'Fiat', 2021, 'RLS3F42', 'Branco', 'Carro', 78300, 2, TRUE, NOW()),
       ('Corolla', 'Toyota', 2020, 'QNY4G11', 'Preto', 'Carro', 112000, 3, TRUE, NOW());

INSERT INTO meta(titulo, formato, valor_atual, valor_desejado, id_usuario, is_ativo)
VALUES ('Presente para a filha', 'SEMANAL', 0, 300.00, 1, TRUE),
	   ('Trocar bateria do celular', 'SEMANAL', 0, 200.00, 2, TRUE),
       ('Jantar em restaurante japonês', 'SEMANAL', 0, 100.00, 3, TRUE);

INSERT INTO custo(tipo, valor, data_vencimento, data_pagamento, descricao, id_veiculo, is_ativo)
VALUES ('Combustível', 100.00, null, '2026-04-02', 'Abastecimento completo', 1, TRUE),
       ('Manutenção', 200.0, null, '2026-03-20', 'Troca das pastilhas de freio dianteiras', 2, TRUE),
       ('Manutenção', 150.0, null, '2026-03-25', 'Alinhamento e balanceamento', 3, TRUE);

INSERT INTO receita_diaria(data_receita, valor, id_usuario, is_ativo)
VALUES ('2026-04-02', 350.00, 1, TRUE),
       ('2026-04-02', 290.75, 2, TRUE),
       ('2026-04-02', 480.10, 3, TRUE);

INSERT INTO manutencao(tipo, data_manutencao, descricao, id_veiculo, id_custo, is_ativo)
VALUES ('Corretiva', '2026-03-20', 'Troca das pastilhas de freio dianteiras', 1, 2, TRUE),
       ('Preventiva', '2026-03-25', 'Alinhamento e balanceamento', 2, 3, TRUE);