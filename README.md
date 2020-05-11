# API Web Service - Lanches Food - Finch Soluções
  Projeto em Spring Boot de uma construção API RESTFul voltado à atender o desafio Finch Soluções <link>http://www.finchsolucoes.com.br/.
   
  Uma solução criada em Java em formato de API REST que atenda aos requisitos para a recepção e/ou criação de pedidos contendo lanches deliciosos aos seus respectivos clientes, onde todos os serviços devem trabalhar com XML e JSON em suas chamadas e retornos.

 #### Stack do projeto
  - Escrito em Java 8;
  - Utilizando as facilidades e recursos framework Spring Boot;
  - Lombok na classes para evitar o boilerplate do Java;
  - Framework Hibernarte e Spring Data JPA para garantir a persistiência dos dados e facilitar as operações CRUD (aumentando o nivel de desempenho e escalabilidade);
  - Boas práticas de programação, utilizando Design Patterns (Builder, Strategy);
  - Testes unitátios (TDD);
  - Banco de dados PostgreSQL;
  - Docker utilizando o compose;
  
  #### Visão Geral
  
  A aplicaçao tem como objetivo disponibilizar endpoints para consulta de informações e operações à respeito de:
  - Lanches (sanduíches em sua maioria);
  - Ingredientes que compõem os lanches ou adicionados como porções extra(s);
  - Pedidos efetuados, com os seus respectivos valores, lanches adicionados e clientes associados. 
  
  #### Instruções Inicialização
  
 O comando ```docker-compose up``` inicializará uma instancia do Postgres 9.3, nesse momento será criada o schema ```food_service``` e suas respectivas tabelas no database ```postgres```.<br> 
 Com a finalidade de gerenciar os lanches e os pedidos com informações necessárias para a demonstração do projeto. <br> Em seguida a aplicação de api-rest-food pode ser executada.
  
  ##### Endpoints: 
  
  Utilizando a ferramenta de documentação de endpoints ```Swagger```, pode-sevisualizar todos os endpoints disponíveis.<br>
  Basta acessar a documentação da API por meio da URL `http://localhost:8080/swagger-ui.html` , logo após a sua inicialização. <br><br> 
  De sorte, segue a lista de alguns endpoints para conhecimento: 
  
  - Retornar uma lista completa de pedidos (JSON/XML):
    - `http://localhost:8080/pedidos/`

 - Retornar uma lista completa de lanches em formatos (JSON/XML):
   - `http://localhost:8080/lanches/`
   - `http://localhost:8080/lanches/exibirTodosEmXML/`
   - `http://localhost:8080/lanches/exibirTodosEmJSON/`
   
 - Retornar uma lista completa de ingrediente em formato (JSON/XML):
   - `http://localhost:8080/ingredientes/`
   - `http://localhost:8080/ingredientes/exibirTodosEmXML/`
   - `http://localhost:8080/ingredientes/exibirTodosEmJSON/`
   
 - Retornar uma lista de pedidos a partir dos filtros como parametros:   
     - `http://localhost:8080/pedidos/buscarPorNomeCliente?nomeCliente=Maria`
     - `http://localhost:8080/pedidos/buscarPorPeriodo?dataInicio=01/01/2020&dataFim=01/06/2020`
     - `http://localhost:8080/pedidos/buscarPorValorIgualOuMaior?valor=10.00`
     
 Entre outros, aos quais podem ser identificados no endereço fornecido pelo Swagger: `http://localhost:8080/swagger-ui.html`.