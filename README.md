# Catalogo Lanches Food - Web Service FINCH SOLUÇÕES
  Projeto em Spring Boot de uma construção API RESTFul voltado à atender o desafio Finch Soluções <link>http://www.finchsolucoes.com.br/.
   
  Uma solução criada em Java em formato de API REST que atenda aos requisitos para a recepção e/ou criação de pedidos contendo lanches deliciosos aos seus respectivos clientes, onde todos os serviços devem trabalhar com XML e JSON em suas chamadas e retornos.

 #### Stack do projeto
  - Escrito em Java 8;
  - Utilizando as facilidades e recursos framework Spring Boot;
  - Lombok na classes para evitar o boilerplate do Java;
  - Framework Hibernarte e Spring Data JPA para garantir a persistiência dos dados e facilitar as operações CRUD (aumentando o nivel de desempenho e escalabilidade);
  - Boas práticas de programação, utilizando Design Patterns (Builder, Strategy);
  - Testes unitátios (TDD);
  - Banco de dados PostgreSQL.
  
  #### Visão Geral
  
  A aplicaçao tem como objetivo disponibilizar endpoints para consulta de informacoes sobre:
  - Lanches (sanduíches em sua maioria);
  - Ingredientes que compõem os lanches ou adicionados como porções extra(s);
  - Pedidos efetuados, com os seus valores, lanches adicionados e clientes associados. 
  
  #### Instruções Inicialização
  
 O comando ```docker-compose up``` inicializará uma instancia do Postgres 9.3, nesse momento será criada o schema ```food_service``` e suas respectivas tabelas no database ```postgres```.<br> 
 Com a finalidade de gerenciar os lanches e os pedidos com informações necessárias para a demonstração do projeto. <br> Em seguida a aplicação de api-rest-food inicializará na porta 8089.
  
  ##### Endpoints: 
  
  Gerenciado pelo ```Swagger```, para visualizar todos os endpoints disponíveis, basta acessar a documentação por meio da URL `http://localhost:8080/swagger-ui.html`


