# Projeto-Final-Web-III
Projeto de um e-commerce utilizando uma arquitetura de microsserviços e Spring WebFlux.

## ✒️ Autor

[Felipe Zanardo](https://github.com/FelipeBZanardo)

## 📋 Enunciado:
Implementar uma API REST de um e-commerce utilizando uma arquitetura de microsserviços. A aplicação deve conter pelo menos 3 microsserviços. A implementação das APIs deve utilizar o Spring WebFlux e deve utilizar pelo menos 3 ferramentas do Spring CLOUD como o Circuit Breaker, o Gateway e o OpenFeign.

## ⚙️ Configurações Iniciais

 * ### cliente-api:
    - Abrir o projeto cliente-api
    - No terminal: 
    `docker compose up`
    - Play no microsserviço

* ### catalogo-api:
  - Abrir o projeto catalogo-api
  - No terminal: 
  `docker compose up` 
  - Play no microsserviço

 * ### email-api:
    - Abrir o projeto email-api
    - No terminal: 
    `docker compose up`
    - Play no microsserviço
  
 * ### pedido-api:
    - Abrir o projeto pedido-api
    - No terminal: 
    `docker compose up`
    - Play no microsserviço
  
 * ### gateway:
    - Abrir o projeto gateway
    - Play na aplicação

 * ### eureka-server:
    - Abrir o projeto gateway
    - Play na aplicação
    - acessar o link: [Eureka](http://localhost:8761/)
  
 * ### Abrir o Arquivo Insomnia e testar as Requisições
   

## 🛠️ Tecnologias Utilizadas

* [IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/) - IDE
* [Spring Initializer](https://start.spring.io/)
* [Maven](https://maven.apache.org/) - Gerenciador de Dependência
* [Docker](https://www.mongodb.com/) 
* [MongoDB](https://www.docker.com/) - Banco de Dados
* [Insomnia](https://insomnia.rest/) - Teste das API's






