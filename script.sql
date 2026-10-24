-- Criação do Banco de Dados do SGU
CREATE DATABASE IF NOT EXISTS db_sgu;
USE db_sgu;

-- Tabela de Funcionários
CREATE TABLE IF NOT EXISTS funcionario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    cargo VARCHAR(50) NOT NULL,
    data_admissao DATE NOT NULL
);

-- Tabela de Uniformes (Produtos)
CREATE TABLE IF NOT EXISTS uniforme (
    id INT AUTO_INCREMENT PRIMARY KEY,
    peca VARCHAR(50) NOT NULL,
    tamanho VARCHAR(10) NOT NULL,
    cor VARCHAR(30) NOT NULL,
    material VARCHAR(50) NOT NULL,
    preco_base DECIMAL(10, 2) NOT NULL
);

-- Tabela de Registro de Produção (Associativa / Transacional)
CREATE TABLE IF NOT EXISTS registro_producao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario INT NOT NULL,
    id_uniforme INT NOT NULL,
    quantidade INT NOT NULL,
    data_producao DATE NOT NULL,
    CONSTRAINT fk_funcionario FOREIGN KEY (id_funcionario) 
        REFERENCES funcionario(id) 
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_uniforme FOREIGN KEY (id_uniforme) 
        REFERENCES uniforme(id) 
        ON DELETE RESTRICT ON UPDATE CASCADE
);