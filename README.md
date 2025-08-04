# Passo a passo de criação de uma API com Spring Boot

## Objetivo:
- Criar uma API REST com Spring Boot
- Implementar o CRUD
- Implementar o Swagger para documentação da API
- Implementar o banco de dados H2

---
## Passo 1: Criar o projeto

- Acessar o site: https://start.spring.io/
- Selecionar as dependências:

    - Lombok
    - Spring Web
    - Spring dev tools
    - Spring Data JPA
    - H2 Database

<img src="https://i.imgur.com/eMPmyvD.png" alt="Spring_initilizr" width="720">

---
## Passo 2: Criar o model

### No exemplo vamos usar um model de produtos:

POJO  (Plain Old Java Object):
- Criar a classe Produto
- Implementar o construtor
- Implementar os métodos get e set
- Implementar o toString
---

## Passo 3: Criar a classe controller
1. Colocar a anotação **@RestController**

   - Essa annotation diz para o java que essa classe é um controller e que ela vai **retornar** um JSON,
   ou seja, receberá requisições rest.


2. Colocar a anotação **@RequestMapping("/produtos")**

   - Essa annotation diz para o java que essa classe vai **responder** por requisições que começam com /produtos.
   - Ou seja, define a URL base da API.

3. Para enviar um produto, vamos usar o método PostMapping:

        @PostMapping
        public Produto salvar(@RequestBody Produto produto) {
        System.out.println("Produto recebido: " + produto);
        return produto;
        }
   Enviar requisição POST e verificar no postman se está funcionando.

   <img src="https://i.imgur.com/DPsRpLK.jpg" alt="Postman screenshot" width="720"/>

       Opções:
        - Body
        - raw
        - JSON
        - Send

Assim, retornará o produto o qual foi enviado em JSON.

O **status 200** OK significa que a requisição foi bem sucedida.

Para poder receber o produto, usamos a anotação **@RequestBody**, que diz para o java que o produto
virá no corpo da requisição.

E ele apresenta o produto no console.

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) { 
    ...
    }

---
## Passo 4: Criar a applicaion.yml ou application.properties
Ex: application.yml: configurações de conexão com o banco de dados.

    spring:
        application:
            name: Produtos API
        datasource:
            url: jdbc:h2:mem:produtos
            username: sa
            password: password
        jpa:
            database-platform: org.hibernate.dialect.H2Dialect
        h2:
            console:
            enabled: true
            path: /h2-console

Ex: application.properties:

    spring.datasource.url=jdbc:h2:mem:produtos
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true

Dessa forma já conseguimos acessar o banco de dados h2 no navegador, com a url: http://localhost:8080/h2-console

<img src="https://i.imgur.com/fuqsePN.png" alt="H2_console" width="500"/>

---
## Passo 5: Criar o arquivo que gera a tabela no bando de dados
- Na pasta resources, criar o arquivo data.sql

        create table produto (
          id varchar(255) not null primary key,
          nome varchar(50) not null,
          descricao varchar(300),
          preco numeric(12,2)
        );
- Essas são as variáveis que vamos usar no model.Produto.

    - No model.Produto, colocar a anotação:

        -  @Entity, que diz para o java que essa classe é uma entidade do banco de dados.
        -  @Table(name="..."), com essa annotation podemos mudar o nome da tabela.
        -  @Id, que diz para o java que essa variável é a chave primária da tabela.
        -  @GeneratedValue, que diz para o java que essa variável é gerada automaticamente pelo banco de dados.
        -  @Column(name="..."), que diz para o java que essa variável é uma coluna da tabela e podemos mudar o nome da coluna.
        -  @NotNull, que diz para o java que essa variável não pode ser nula.

- Annotations:

      (...)
      @Entity 
      @Table(name = "produto")
      public class Produto {

        @Id
        @Column(name = "id")
        private String id;

        @Column(name = "descricao")
        private String descricao;
      
        @Column(name = "preco")
        private Double preco;
        (...)


Com o data.sql criado, podemos rodar a aplicação e, caso nao tenha nenhum erro, veremos que a tabela foi criada no banco de dados.

<img src="https://i.imgur.com/SZxeOek.png" alt="H2_console_produtos" width="750"/>
