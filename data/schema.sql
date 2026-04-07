DROP DATABASE IF EXISTS bd_projeto;

CREATE DATABASE IF NOT EXISTS bd_projeto;

USE bd_projeto;

CREATE TABLE usuario(
	id_usuario BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    data_cadastro DATE NOT NULL,
    telefone VARCHAR(30) NOT NULL,
    PRIMARY KEY(id_usuario));
    
CREATE TABLE meta(
	id_meta BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    formato VARCHAR(200) NOT NULL,
    valor DOUBLE NOT NULL,
    id_usuario BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY(id_meta),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE veiculo(
	id_veiculo BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    ano INTEGER NOT NULL,
    placa VARCHAR(7) NOT NULL UNIQUE,
    cor VARCHAR(80) NOT NULL,
    tipo VARCHAR(80) NOT NULL,
    km_atual INTEGER NOT NULL,
    data_ultima_atualizacao_km DATE NOT NULL,
    id_usuario BIGINT UNSIGNED UNIQUE NOT NULL,
    PRIMARY KEY(id_veiculo),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
    ON DELETE CASCADE	
    ON UPDATE CASCADE);

CREATE TABLE receita_diaria(
	id_receita BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    data_receita DATE NOT NULL,
    valor DOUBLE NOT NULL,
    id_usuario BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY(id_receita),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE custo(	
	id_custo BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    tipo VARCHAR(150) NOT NULL,
    valor DOUBLE NOT NULL,
    data_vencimento DATE,
    data_pagamento DATE,
    descricao VARCHAR(350) NOT NULL,
    id_veiculo BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY(id_custo),
    FOREIGN KEY(id_veiculo) REFERENCES veiculo(id_veiculo)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE manutencao(
	id_manutencao BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    tipo VARCHAR(200) NOT NULL,
    data_manutencao DATE NOT NULL,
    descricao VARCHAR(350),
    id_veiculo BIGINT UNSIGNED NOT NULL,
    id_custo BIGINT UNSIGNED NOT NULL,
    PRIMARY KEY(id_manutencao),
    FOREIGN KEY(id_veiculo) REFERENCES veiculo(id_veiculo) 
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    FOREIGN KEY(id_custo) REFERENCES custo(id_custo)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

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

SELECT * FROM usuario;

SELECT * FROM  veiculo;

SELECT * FROM  meta;

SELECT * FROM  custo;

SELECT * FROM receita_diaria;

SELECT * FROM manutencao;



