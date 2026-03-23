CREATE DATABASE bd_projeto;

USE bd_projeto;

CREATE TABLE usuario(
	id_usuario INTEGER AUTO_INCREMENT NOT NULL,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(50) NOT NULL,
    data_cadastro DATE NOT NULL,
    PRIMARY KEY(id_usuario));
    
CREATE TABLE veiculo(
	id_veiculo INTEGER AUTO_INCREMENT NOT NULL,
    modelo VARCHAR(20) NOT NULL,
    marca VARCHAR(15) NOT NULL,
    ano INTEGER NOT NULL,
    placa VARCHAR(7) NOT NULL UNIQUE,
    cor VARCHAR(15) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    km_atual INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    PRIMARY KEY(id_veiculo),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE corrida(
	id_corrida INTEGER AUTO_INCREMENT NOT NULL,
    data_hora_inicio DATETIME NOT NULL,
    data_hora_fim DATETIME NOT NULL,
    km_inicial INTEGER NOT NULL,
    km_final INTEGER NOT NULL,
    valor_ganho DECIMAL(10,2) NOT NULL,
    id_usuario INTEGER NOT NULL,
    id_veiculo INTEGER NOT NULL,
    PRIMARY KEY(id_corrida),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY(id_veiculo) REFERENCES veiculo(id_veiculo));
    
CREATE TABLE custo(	
	id_custo INTEGER AUTO_INCREMENT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_custo DATE NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    id_veiculo INTEGER NOT NULL,
    PRIMARY KEY(id_custo),
    FOREIGN KEY(id_veiculo) REFERENCES veiculo(id_veiculo));
    
CREATE TABLE manutencao(
	id_manutencao INTEGER AUTO_INCREMENT NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    data_manutencao DATE NOT NULL,
    id_veiculo INTEGER NOT NULL,
    PRIMARY KEY(id_manutencao),
    FOREIGN KEY(id_veiculo) REFERENCES veiculo(id_veiculo));
