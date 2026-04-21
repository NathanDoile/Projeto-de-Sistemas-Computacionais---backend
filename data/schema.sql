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
    notificacaoVencimento BOOLEAN NOT NULL,
    notificacaoManutencao BOOLEAN NOT NULL,
    is_ativo BOOLEAN NOT NULL,
    PRIMARY KEY(id_usuario));

CREATE TABLE meta(
    id_meta BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    formato VARCHAR(200) NOT NULL,
    valor DOUBLE NOT NULL,
    id_usuario BIGINT UNSIGNED NOT NULL,
    is_ativo BOOLEAN NOT NULL,
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
    is_ativo BOOLEAN NOT NULL,
    PRIMARY KEY(id_veiculo),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE);

CREATE TABLE receita_diaria(
    id_receita BIGINT UNSIGNED AUTO_INCREMENT NOT NULL,
    data_receita DATE NOT NULL,
    valor DOUBLE NOT NULL,
    id_usuario BIGINT UNSIGNED NOT NULL,
    is_ativo BOOLEAN NOT NULL,
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
    is_ativo BOOLEAN NOT NULL,
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
    is_ativo BOOLEAN NOT NULL,
    PRIMARY KEY(id_manutencao),
    FOREIGN KEY(id_veiculo) REFERENCES veiculo(id_veiculo)
       ON DELETE CASCADE
       ON UPDATE CASCADE,
    FOREIGN KEY(id_custo) REFERENCES custo(id_custo)
       ON DELETE CASCADE
       ON UPDATE CASCADE);