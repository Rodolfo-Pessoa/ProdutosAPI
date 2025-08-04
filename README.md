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
## Passo 2: Criar o model (modelo de negócio)

### No exemplo vamos usar um model de produtos:

POJO  (Plain Old Java Object):
- Criar a classe Produto
- Implementar o construtor
- Implementar os métodos get e set
- Implementar o toString
---

## Passo 3: Criar a classe controller (é a API)
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
---
## Passo 6: Criar o repositório (comunicação com o banco de dados)

- Essa interface é responsável por fazer a comunicação com o banco de dados.
- Criar a interface ProdutoRepository que extende JpaRepository<Produto, String>

      public interface ProdutoRepository extends JpaRepository<Produto, String> {
      }

- O JpaRepository<Produto, String> é uma interface do Spring Data JPA que fornece métodos para realizar operações CRUD (Create, Read, Update, Delete) em uma entidade.

- O JpaRepository<Produto, String> recebe dois parâmetros:
    - O primeiro é a entidade que queremos persistir no banco de dados.
    - O segundo é o tipo da chave primária da entidade, como o ID está sendo representado por uma String, o segundo parâmetro será String.
    - Ficando então: <Produto, String>

---
## Passo 7: Injetar a classe repository no controller

1. Injetar a classe ProdutoRepository no controller:

          @Autowired
          private ProdutoRepository produtoRepository;

2. Criar o construtor desse repository:

        public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
        }
3. O próximo passo é implementar a instância do repository no método dentro do controller.
    
   Que ficará dessa forma:

           @PostMapping
           public Produto salvar(@RequestBody Produto produto) {
           System.out.println("Produto recebido: " + produto);
           String id = UUID.randomUUID().toString();

           produto.setId(id);
        
           produtoRepository.save(produto);
           return produto;
       }
String id = UUID.randomUUID().toString(); -> Gera um id aleatório para o produto.
    
- Como colocamos que o ID no BD é NOTNULL, precisamos atribuir um id para o produto.
- O UUID.randomUUID().toString() gera um id aleatório e o toString() converte para String.

produto.setId(id); -> Seta o id gerado no produto.

repository.save(produto); -> Salva o produto no banco de dados.

Assim, quando subimos a aplicação e fazemos uma requisição POST para a rota /produtos, o produto será salvo no banco de dados e retornará um JSON com um ID aleatório:

    {
    "id": "966c6149-3b92-41f0-b812-5bcf1c459a71",
    "nome": "Pendrve",
    "descricao": "Kingston 1TB",
    "preco": 50.0
    }

---
## Passo 8: Criar um endpoint e um método para retornar os dados do produto

- 8.1 Retornar os dados do produto pelo ID:

      @GetMapping("/{id}")
      public Produto obterPorId(@PathVariable("id") String id) {
      return produtoRepository.findById(id).orElse(null);
      }

  - Obter o ID da URL com @PathVariable("id") String id
  - Retornar o produto pelo ID com produtoRepository.findById(id).orElse(null);
  - O orElse(null) é para retornar null caso o produto não seja encontrado.

- 8.2 Criar uma nova Request no postman, nesse caso será uma requisição GET para a rota /produtos/{id}:

<img src="https://i.imgur.com/ulSXcpk.png" alt="getById_postman" width="750"/>

- 8.3 Testar a requisição GET para a rota /produtos/{id}:

  http://localhost:8080/produtos/966c6149-3b92-41f0-b812-5bcf1c45

---
## Passo 9: Criar o método delete

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable("id") String id) {
    produtoRepository.deleteById(id);
    }
Além disso, criar uma nova Request no postman, nesse caso será uma requisição DELETE para a rota /produtos/{id}:

<img src="https://i.imgur.com/qdrKhTe.png" alt="delete_postman" width="750"/>

---
## Passo 

     