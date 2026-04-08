USE bd_projeto;

INSERT INTO usuario(nome, email, senha, data_cadastro, telefone)
VALUES ('Adriano', 'adriano@gmail.com', 'a4Gj@j21', '2026-01-11', '00 00000-0000'),
       ('Beto', 'beto@gmail.com', 'dWu!87y8', '2025-10-17', '00 00000-0001'),
       ('Carla', 'carla@gmail.com', 'ac@g78Er', '2025-08-21', '00 00000-0002');

INSERT INTO veiculo(modelo, marca, ano, placa, cor, tipo, km_atual, data_ultima_atualizacao_km, id_usuario)
VALUES ('Onix Plus', 'Chevrolet', 2022, 'BTA2E19', 'Prata', 'Carro', 45200, '2026-04-01', 1),
       ('Cronos', 'Fiat', 2021, 'RLS3F42', 'Branco', 'Carro', 78300, '2026-03-28', 2),
       ('Corolla', 'Toyota', 2020, 'QNY4G11', 'Preto', 'Carro', 112000, '2026-04-03', 3);

INSERT INTO meta(titulo, formato, valor, id_usuario)
VALUES ('Presente para a filha', 'Semanal', 300.0, 1),
	   ('Trocar bateria do celular', 'Semanal', 200.0, 2),
       ('Jantar em restaurante japonês', 'Semanal', 300.0, 3);

INSERT INTO custo(tipo, valor, data_vencimento, data_pagamento, descricao, id_veiculo)
VALUES ('Combustível', 100.00, null, '2026-04-02', 'Abastecimento completo', 1),
       ('Manutenção', 200.0, null, '2026-03-20', 'Troca das pastilhas de freio dianteiras', 2),
       ('Manutenção', 150.0, null, '2026-03-25', 'Alinhamento e balanceamento', 3);

INSERT INTO receita_diaria(data_receita, valor, id_usuario)
VALUES ('2026-04-02', 350.00, 1),
       ('2026-04-02', 290.75, 2),
       ('2026-04-02', 480.10, 3);

INSERT INTO manutencao(tipo, data_manutencao, descricao, id_veiculo, id_custo)
VALUES ('Corretiva', '2026-03-20', 'Troca das pastilhas de freio dianteiras', 1, 2),
       ('Preventiva', '2026-03-25', 'Alinhamento e balanceamento', 2, 3);