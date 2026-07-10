# Projeto de Desenvolvimento de Sistemas

### Integrantes
- **Nome e Linkedin**
  - | Professor Remor | (https://www.linkedin.com/in/rodrigoremor) |
  
  - **Front-End:**
  - | Ariel | (https://www.linkedin.com/in/campos-ariel/) |
  - | Laura | (https://www.linkedin.com/in/laura-a-ferreira) |
  - | Jackson | (https://www.linkedin.com/in/jackson-gonçalves-nervis-1392b4421) |
  
  - **Back-End:**
  - | Nathan | (https://www.linkedin.com/in/nathan-doile) |
  - | Felipe | (https://www.linkedin.com/in/felipe-h-martins) |
  - | Ricardo | (https://www.linkedin.com/in/ricardobit) |
  - | Leandro | (https://www.linkedin.com/in/leandro-martens-7523b0332/) |
  
  - **Design:**
  - | Ana | (https://www.linkedin.com/in/anaclaudiamoura8) |
  - | Lucas | (https://www.linkedin.com/in/lucasbertoluci/) |
  
  - **Scrum-master:**
  - | Arthur | (https://www.linkedin.com/in/arthur-vieira-colaço-289a99202) |
  - | Carol | (https://www.linkedin.com/in/carolina-pinheiro-de-souza-1826b1258) |

### Descrição do Projeto
- **O Projeto**
  - Este projeto consiste no desenvolvimento de um aplicativo de controle financeiro e gerenciamento para motoristas de aplicativo. 
  - Seu objetivo é auxiliar os usuários na organização de suas atividades, oferecendo ferramentas para acompanhar receitas, despesas e metas financeiras de forma prática e intuitiva.
  - O aplicativo permite cadastrar veículos, registrar manutenções, controlar receitas e custos, definir e acompanhar metas financeiras, além de gerar relatórios que ajudam o motorista a analisar seu desempenho e tomar decisões para melhorar sua rentabilidade.

### Endereçamentos

- [Modelagem Conceitual](data/modelagem_projeto.png)
- [Coleção do Postman](data/POSTMAN%20PROJETO.postman_collection.json)
- [Schema do Banco de Dados](data/schema.sql)
- [Scripts de Inserts](data/insert.sql)
- [Consultas SQL (SELECT)](data/select.sql)

### Tecnologias Utilizadas
- **| Tecnologia | Finalidade |**
 - | Java 17 | Linguagem de programação |
 - | Spring Boot 4 | Desenvolvimento da API |
 - | Spring Web MVC | Criação dos endpoints REST |
 - | Spring Data JPA | Persistência de dados |
 - | Spring Security | Autenticação e autorização |
 - | Spring Validation | Validação de dados |
 - | Maven | Gerenciamento de dependências |
 - | MySQL | Banco de dados |
 - | Lombok | Redução de código repetitivo |
 - | JasperReports | Geração de relatórios |
 - | Postman | Testes da API |
 - | Git e GitHub | Controle de versão |
 - | IntelliJ IDEA | Ambiente de desenvolvimento |
 - | brModelo | Modelagem conceitual do banco de dados |

### Estrutura do Projeto

O backend é um projeto Spring Boot organizado como uma aplicação Maven com pacote base `br.edu.ifsul.sapucaia.projeto`. A estrutura principal segue uma arquitetura em camadas, separando responsabilidades entre controles, serviços, persistência, domínio, segurança, validação e utilitários.

- `src/main/java/br/edu/ifsul/sapucaia/projeto`
  - `controller/`: REST controllers que expõem as rotas da API para usuários, veículos, custos, manutenções, receitas diárias, metas, relatórios e integração com IA.
  - `service/`: serviços de negócio, com subpacotes por domínio (`usuario`, `veiculo`, `custo`, `manutencao`, `receita_diaria`, `meta`, `relatorios`, `ia`, `validator`). Cada classe de serviço encapsula regras de negócio, orquestração e validações.
  - `repository/`: interfaces Spring Data JPA para acesso ao banco de dados e operações CRUD das entidades do sistema.
  - `domain/`: entidades JPA que representam as tabelas do banco de dados e os respectivos enums de tipos e formatos usados pelo sistema.
  - `security/`: configurações de segurança, controllers e serviços para login, autenticação, redefinição de senha e envio de e-mails.
  - `config/`: configuração global do Spring e tratamento de exceções via `ApiExceptionHandler`.
  - `helper/`: classes utilitárias para manipulação de datas, períodos e outros componentes comuns.
  - `mapper/`: mapeamento entre requests/responses e entidades de domínio.
  - `validator/`: validadores customizados para regras específicas do domínio e da interface.
  - `exception/`: definições de exceções aplicadas ao fluxo de negócio.
  - `ProjetoApplication.java`: classe principal que inicializa a aplicação Spring Boot.

- `src/main/resources/`
  - `application.yml`: configurações da aplicação e do ambiente.
  - `manutencoes.jrxml`: template JasperReports usado na geração de relatórios.
  - `templates/`: arquivos de template adicionais usados pela aplicação.

- `src/test/java/`: testes automatizados para serviços, validações, segurança e componentes da aplicação.

- `pom.xml`: definição das dependências do Maven, incluindo Spring Boot, Spring Data JPA, Spring Security, validação, JasperReports, MySQL e H2 para testes.

- `data/`: documentação de suporte com modelagem, scripts SQL de criação, inserção de dados e consultas, além da coleção Postman do projeto.

### Endpoints

### Rota Principal:

- **Login do usuário**
    - `POST /login`

      ```Basic Auth
        "email": "adriano@gmail.com",
        "password": "123"
      ```

- **Logout do usuário**
    - `POST /logout`

### Rota: `/usuario`

- **Cria um usuário**
  - `POST /`

    ```json
    {
      "nome": "Primeiro Usuário",
      "email": "testando0@gmail.com",
      "senha": "testando123",
      "telefone": "51999999999"
    }
    ```

- **Altera a senha de um usuário**
  - Precisa estar logado para utilizar esse path
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `PUT /{id}/alterar-senha`

    ```json
    {
      "senhaAtual": "testando123",
      "novaSenha": "atualizandoSenha123"
    }
    ```

- **Edita o perfil de um usuário**
  - Precisa estar logado para utilizar esse path
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `PUT /{id}/editar-perfil`

    ```json
    {
      "nome": "Nome Atualizado",
      "email": "novo@email.com",
      "telefone": "51999999999"
    }
    ```

    ```json
    {
      "email": "novo@email.com"
    }
    ```

    ```json
    {
      "telefone": "51999999999"
    }
    ```

- **Exclui a conta de um usuário**
  - Precisa estar logado para utilizar esse path
  - `DELETE /excluir-conta`

    ```json
    {
      "senha": "senha123"
    }
    ```
- **Ativa/Desativa notificações**
  - Precisa estar logado para utilizar esse path
  - Altera a configuração de notificações do usuário
  - Parâmetros de URL: `id`, é o identificador do usuário
  - `PUT /notificacoes/{id}`

    ```json
    {
       "notificacao": "Vencimento"
    }
    ```

### Rota: `/security`

- **Enviar código de redefinição de senha**
  - Não precisa estar logado para utilizar esse path
  - `POST /enviar-codigo`

    ```json
    {
      "email": "usuario@email.com"
    }
    ```

- **Validar código de redefinição de senha**
  - Não precisa estar logado para utilizar esse path
  - `POST /validar-codigo`

    ```json
    {
      "email": "usuario@email.com",
      "codigo": "ABC12345"
    }
    ```

- **Redefinir senha**
  - Não precisa estar logado para utilizar esse path
  - `PUT /redefinir-senha`

    ```json
    {
      "email": "usuario@email.com",
      "codigo": "ABC12345",
      "senha": "novaSenha@123"
    }
    ```

### Rota: `/receita-diaria`

- **Cria uma receita diária**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
      "dataReceita": "Nome Sobrenome",
      "valor": 200,
      "idUsuario": 1
    }
    ```

### Rota: `/meta`

- **Cria uma meta**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
      "titulo": "Ingressos para show fim de semana",
      "formato": "Semanal",
      "valor": 300,
      "idUsuario": 1
    }
    ```

- **Buscar metas**
    - Precisa estar logado para utilizar esse path
    - `GET /`

- **Excluir meta**
    - Precisa estar logado para utilizar esse path
    - `DELETE /{id}`
    - Parâmetros de URL: `id`, é o identificador da meta

### Rota: `/custo`

- **Cria um custo**
  - Precisa estar logado para utilizar esse path
  - `POST /`

    ```json
    {
      "tipo": "gasolina",
      "valor": 500,
      "dataVencimento": "2026-06-21",
      "dataPagamento": "2026-06-18",
      "descricao": "Custo de gasolina"
    }
    ```  

- **Edita o custo de um usuário**
    - Precisa estar logado para utilizar esse path
    - `PUT /editar`

      ```json
      {
        "idCusto": 1,
        "tipo": "manutencao",
        "valor": 295.00,
        "dataVencimento": "2026-04-23",
        "dataPagamento": "2026-04-15",
        "descricao": "manutencao do carro"
      }
      ``` 


- **Buscar custos em aberto/não pagos**
    - Precisa estar logado para utilizar esse path;
    - Parâmetros de URL: `page`, é qual página de custos quer buscar;
    - Parâmetros de URL: `size`, é o tamanho da lista de custos que quer mostrar em cada página;
    - `GET /{id}/em-aberto?page=0$size=2`

### Rota: `/veiculo`
- Cria um veiculo;
    - Precisa estar logado para utilizar esse path;
    - `POST /`

        ```json
          {
                  "marca":"VW",
                  "modelo":"ONIX",
                  "placa":"ABC1A23",
                  "tipo":"carro",
                  "ano":2023,
                  "cor":"azul",
                  "kmAtual":23589,
                  "idUsuario":1
          }
        ```   

- **Retornar dados do veículo**
  - Precisa estar logado para utilizar esse path;
  - `GET`

- **Obter informações de manutenção do veículo**
  - Precisa estar logado para utilizar esse path
  - `GET /informacoes-manutencao`

    Retorna:
    ```json
    {
      "totalManutencoesPreventivas": 2,
      "totalManutencoesCorretivas": 1,
      "totalManutencoesPreditivas": 0,
      "valorTotalPreventivas": 300.00,
      "valorTotalCorretivas": 500.00,
      "valorTotalPreditivas": 0.00,
      "mediaPrecoManutencao": 266.67,
      "valorCustoPorKmRodado": 0.012
    }
    ```


- Atualizar quilometragem do veículo
  - Precisa estar logado para utilizar esse path;
  - `PUT /atualizar-km`
      
      ```json
            {
                    "kmAtualizado":45300
            }
      ```   

### Rota: `/manutencao`
- Cria uma manutencao;
    - Precisa estar logado para utilizar esse path;
        - `POST /`

            ```json
              {
                      "tipo":"preventiva",
                      "dataManutencao": "2026-04-02",
                      "descricao":"fiz uma manutencao",
                      "idVeiculo":1,
                      "valor":300
              }
            ```
          
- Lista as manutenções:
  - Precisa estar logado para utilizar essa path;
  - Parâmetros de URL: `page`, é qual página de custos quer buscar;
  - Parâmetros de URL: `size`, é o tamanho da lista de custos que quer mostrar em cada página;
    - `GET ?page=1$size=10`
          
### Rota: `/relatorios`
- **Informações de Semana**
  - Precisa estar logado para utilizar esse path;
  - `GET /informacoes-semana`

- **Receita da Semana**
  - Precisa estar logado para utilizar esse path;
  - `GET /receita-semana`

- **Resumo Financeiro**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: tipo (dia | semana | mes) e data (opcional, formato YYYY-MM-DD)
  - `GET /resumo-financeiro`  

- **Gasto por categoria**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: tipo (dia | semana | mes | ano) e dataBase (formato YYYY-MM-DD)
  - `GET /gastos-categoria-dia`

- **Últimas transações**
  - Precisa estar logado para utilizar esse path;
  - `GET /ultimas-transacoes`

- **Exportar Relatório de Manutenções**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: idVeiculo, tipoPeriodo(semana, mes e ano) e dataReferencia(Formato YYYY-MM-DD)
  - `GET /exportar/manutencoes`

- **Exportar Relatório Financeiro**
  - Precisa estar logado para utilizar esse path;
  - Parâmetros de query: dataReferencia(Formato YYYY-MM-DD) e periodo(semanal, mensal e anual)
  - `GET /exportar/financeiro`

- **Verificar pendência do usuário com o sistema**
  - Precisa estar logado para utilizar esse path;
  - `GET /pendencias`