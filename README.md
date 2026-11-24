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
* **Gestão de Funcionários (CRUD 1):** Cadastro, edição, listagem e remoção de colaboradores da fábrica, com consulta por nome (busca parcial, sem diferenciar maiúsculas de minúsculas).
* **Catálogo de Uniformes/Produtos (CRUD 2):** Cadastro e manutenção das peças fabricadas (tipo, tamanho, cor, tecido e preço base), com **sugestão automática de preço** conforme o material e o tamanho escolhidos.
* **Controle de Ordens de Produção (CRUD 3):** Registro diário/semanal de quantidade de peças produzidas por funcionário, com listagem do histórico e exclusão de lançamentos incorretos.
* **Consultas Analíticas:** Filtragem de produção por períodos/peças e acompanhamento do desempenho individual dos colaboradores. *(🚧 em desenvolvimento)*
* **Relatórios Gerenciais:** Emissão de documentos em PDF via JasperReports para tomada de decisão (Produtividade Mensal e Catálogo de Peças). *(🚧 em desenvolvimento: as opções já existem no menu "Relatórios" e exibem aviso de que ainda não foram implementadas)*

### ❌ Limitações do Escopo (O que o sistema NÃO faz):
* Gestão de vendas ou atendimento a clientes externos.
* Controle de estoque de matérias-primas (fios, linhas, botões, tecidos).
* Processamento de folha de pagamento ou cálculo de salários.

---

## 🖥️ Telas e Funcionalidades

A janela principal (`MainView.fxml`) exibe uma tela de boas-vindas e um menu superior. Cada opção do menu carrega a tela correspondente na região central da janela.

| Menu | Opção | Tela | Situação |
|------|-------|------|----------|
| Cadastros | Funcionários | `FuncionarioView.fxml` | ✅ Implementada |
| Cadastros | Uniformes | `UniformeView.fxml` | ✅ Implementada |
| Produção | Registrar Produção | `ProducaoView.fxml` | ✅ Implementada |
| Relatórios | Produtividade | - | 🚧 Em desenvolvimento |
| Relatórios | Catálogo | - | 🚧 Em desenvolvimento |

### Funcionários
* Formulário com nome, CPF, cargo e data de admissão; tabela com os funcionários cadastrados.
* Botões **Salvar** (insere ou atualiza, conforme haja uma linha selecionada), **Consultar** (filtra por nome), **Limpar** e **Excluir** (com confirmação).
* Ao selecionar uma linha da tabela, o formulário é preenchido para edição.
* O CPF é exibido formatado (`000.000.000-00`) e gravado somente com os 11 dígitos.

### Uniformes
* Formulário com peça, tamanho, cor, material e preço; tabela com os uniformes cadastrados.
* Botões **Novo**, **Salvar** (insere ou atualiza) e **Excluir** (com confirmação).
* Ao escolher material e tamanho, o preço sugerido é preenchido automaticamente e pode ser ajustado pelo usuário.
* O preço é exibido como moeda (ex.: `R$ 35,00`) e aceita vírgula ou ponto na digitação.

### Registrar Produção
* Formulário com funcionário, uniforme, quantidade e data de produção (padrão: data atual); tabela com o histórico de registros.
* Botões **Novo**, **Salvar Produção** e **Excluir Registro** (com confirmação).
* Não há edição: um lançamento incorreto deve ser excluído e registrado novamente.

---

## 📏 Regras de Negócio

* **Funcionário:** nome, cargo e data de admissão são obrigatórios; o CPF deve ter 11 dígitos (a máscara é ignorada).
* **Uniforme:** peça, tamanho, cor e material são obrigatórios e o preço deve ser maior que zero.
* **Produção:** funcionário, uniforme e data são obrigatórios; a quantidade deve ser um número inteiro maior que zero.
* **Preço sugerido do uniforme:**
  * material não informado: R$ 20,00;
  * Algodão: R$ 35,00;
  * Poliéster: R$ 25,00;
  * tamanho **GG**: acréscimo de R$ 5,00.
* **Opções fixas:**
  * Peça: Camiseta, Calça e Boné;
  * Tamanho: P, M, G e GG;
  * Material: Algodão e Poliéster;
  * Cor: Branco, Preto, Azul, Vermelho, Verde e Amarelo.
* **Integridade dos dados:** não é possível excluir um funcionário ou um uniforme que possua registros de produção vinculados (chave estrangeira); o sistema avisa o usuário nesses casos.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem de Programação:** Java (JavaSE-25 / JDK 17+)
* **Interface Gráfica:** JavaFX (telas em FXML, estilizadas com CSS)
* **Banco de Dados:** MySQL (acesso via JDBC)
* **Relatórios:** JasperReports / Jaspersoft Studio
* **Gerenciamento e Versionamento:** Git e GitHub

---

## 📁 Estrutura de Pastas do Projeto

A aplicação adota o padrão de arquitetura em camadas MVC (Model-View-Controller) aliado ao padrão DAO (Data Access Object), organizado na seguinte estrutura de pacotes:

```text
TrabalhoFinal/
└── src/
    ├── app/          # Classe principal (Main) que inicializa a aplicação JavaFX
    │   └── App.java
    ├── controller/   # Controladores JavaFX que gerenciam a lógica da interface gráfica
    │   ├── MainController.java          # Navegação entre as telas (menu)
    │   ├── FuncionarioController.java
    │   ├── UniformeController.java
    │   └── ProducaoController.java
    ├── css/          # Folha de estilo única, usada por todas as telas
    │   └── estilo.css
    ├── dao/          # Camada de persistência de dados (operações de CRUD em SQL no MySQL)
    │   ├── Conexao.java                 # Fornece as conexões com o banco db_sgu
    │   ├── FuncionarioDAO.java
    │   ├── UniformeDAO.java
    │   └── RegistroProducaoDAO.java
    ├── imgs/         # Recurso de imagens, ícones e assets visuais do sistema
    │   ├── icone_uniforme.png
    │   └── uniforme-de-futebol.png
    ├── model/        # Classes de domínio de Orientação a Objetos (Funcionario, Uniforme, etc.)
    │   ├── Funcionario.java
    │   ├── Uniforme.java
    │   ├── RegistroProducao.java
    │   ├── Peca.java                    # Enum: CAMISETA, CALCA, BONE
    │   ├── Tamanho.java                 # Enum: P, M, G, GG
    │   └── Material.java                # Enum: ALGODAO, POLIESTER
    ├── relatorios/   # Arquivos compilados do JasperReports (.jrxml / .jasper)
    └── view/         # Interfaces gráficas desenvolvidas em FXML
        ├── MainView.fxml
        ├── FuncionarioView.fxml
        ├── UniformeView.fxml
        └── ProducaoView.fxml
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
   ```
4. Confira o usuário e a senha do MySQL na classe `dao/Conexao.java` (conexão padrão: `localhost:3306`, banco `db_sgu`) e ajuste se forem diferentes na sua máquina.

### Tabelas

| Tabela | Colunas |
|--------|---------|
| `funcionario` | `id` (PK), `nome`, `cpf`, `cargo`, `data_admissao` |
| `uniforme` | `id` (PK), `peca`, `tamanho`, `cor`, `material`, `preco_base` |
| `registro_producao` | `id` (PK), `id_funcionario` (FK), `id_uniforme` (FK), `quantidade`, `data_producao` |

* Um funcionário pode ter vários registros de produção (1:N) e um uniforme também (1:N).
* Peça, tamanho e material são gravados pelo **nome da constante** do enum (ex.: `CAMISETA`, `GG`, `ALGODAO`); o texto amigável exibido nas telas (ex.: "Camiseta", "Algodão") não altera o valor salvo no banco.
* O CPF é armazenado somente com os dígitos (11 caracteres).

---

## 👥 Integrantes da Equipe

*  João Victor Ferreira Pimenta
*  Gustavo Henrique de Oliveira Rodrigues Silva
*  Luigi Ribeiro de Moraes
