[TrabalhoFinal_README.md](https://github.com/user-attachments/files/33383014/TrabalhoFinal_README.md)
# SGU - Sistema de Gestão de Uniformes

> **Disciplina:** Programação Orientada a Objetos 2 (POO 2)  
> **Professor:** Paulo Francisco  
> **Instituição:** Instituto Federal de Goiás - Campus Inhumas  
> **Projeto Final - 4º Bimestre**

---

## 📌 Sobre o Projeto

O **SGU (Sistema de Gestão de Uniformes)** é uma aplicação desktop desenvolvida para automatizar e otimizar o acompanhamento da produção interna de uma confecção de uniformes.

O sistema resolve o problema da falta de rastreabilidade de produtividade na fábrica, permitindo registrar quais funcionários fabricaram determinados modelos de peças em datas específicas, permitindo o acompanhamento do volume total produzido e a geração de métricas de desempenho.

---

## 🎯 Escopo do Sistema

### ✅ O que o sistema faz:
* **Gestão de Funcionários (CRUD 1):** Cadastro, edição, listagem e remoção de colaboradores da fábrica.
* **Catálogo de Uniformes/Produtos (CRUD 2):** Cadastro e manutenção das peças fabricadas (tipo, tamanho, cor, tecido e preço base).
* **Controle de Ordens de Produção (CRUD 3):** Registro diário/semanal de quantidade de peças produzidas por funcionário.
* **Consultas Analíticas:** Filtragem de produção por períodos/peças e acompanhamento do desempenho individual dos colaboradores.
* **Relatórios Gerenciais:** Emissão de documentos em PDF via JasperReports para tomada de decisão (Produtividade Mensal e Catálogo de Peças).

### ❌ Limitações do Escopo (O que o sistema NÃO faz):
* Gestão de vendas ou atendimento a clientes externos.
* Controle de estoque de matérias-primas (fios, linhas, botões, tecidos).
* Processamento de folha de pagamento ou cálculo de salários.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem de Programação:** Java (JavaSE-25 / JDK 17+)
* **Interface Gráfica:** JavaFX
* **Banco de Dados:** MySQL
* **Relatórios:** JasperReports / Jaspersoft Studio
* **Gerenciamento e Versionamento:** Git e GitHub

---

## 📁 Estrutura de Pastas do Projeto

A aplicação adota o padrão de arquitetura em camadas MVC (Model-View-Controller) aliado ao padrão DAO (Data Access Object), organizado na seguinte estrutura de pacotes:

```text
TrabalhoFinal/
└── src/
    ├── app/          # Classe principal (Main) que inicializa a aplicação JavaFX
    ├── controller/   # Controladores JavaFX que gerenciam a lógica da interface gráfica
    ├── dao/          # Camada de persistência de dados (operações de CRUD em SQL no MySQL)
    ├── imgs/         # Recurso de imagens, ícones e assets visuais do sistema
    ├── model/        # Classes de domínio de Orientação a Objetos (Funcionario, Uniforme, etc.)
    ├── relatorios/   # Arquivos compilados do JasperReports (.jrxml / .jasper)
    └── view/         # Interfaces gráficas desenvolvidas em FXML
```

---

## 🗄️ Configuração do Banco de Dados

O sistema utiliza o banco de dados MySQL. Siga os passos abaixo para configurar a base de dados:

1. Certifique-se de ter o MySQL Server instalado e rodando em sua máquina.
2. Abra o **MySQL Workbench** (ou sua ferramenta de preferência).
3. Execute o script SQL fornecido na raiz do projeto (`script.sql`), que criará automaticamente o banco `db_sgu` e as tabelas necessárias:
   ```sql
   -- O script contém os comandos para criação do banco e das tabelas:
   -- funcionario, uniforme e registro_producao.

---

## 👥 Integrantes da Equipe

*  João Victor Ferreira Pimenta
*  Gustavo Henrique de Oliveira Rodrigues Silva
*  Luigi Ribeiro de Moraes
