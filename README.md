# Projeto-Final-Web-III
Projeto de um e-commerce utilizando uma arquitetura de microsserviços e Spring WebFlux.

## ✒️ Autor

[Felipe Zanardo](https://github.com/FelipeBZanardo)

## 📋 Enunciado:
Implementar uma API REST de um e-commerce utilizando uma arquitetura de microsserviços. A aplicação deve conter pelo menos 3 microsserviços. A implementação das APIs deve utilizar o Spring WebFlux e deve utilizar pelo menos 3 ferramentas do Spring CLOUD como o Circuit Breaker, o Gateway e o OpenFeign.

## 🧾 Sobre o Projeto:
- Quatro microsserviços implementados:

### 1) cliente-api:
    - Capaz de criar e buscar um cliente;
    - Para criar um Cliente é preciso: um nome e um e-mail;
    - 🚨Passar um e-mail REAL no momento do cadastro de Cliente. Esse e-mail receberá uma mensagem quando o pedido for ENVIADO.
    
### 2) catalogo-api:
    - Capaz de criar, buscar e listar produtos, além de poder atualizar a quantidade no estoque;
    - Para criar um Produto é preciso: nome, preço e quantidade no estoque.
    
### 3) email-api:
    - Capaz de enviar um e-mail;
    - Para enviar um e-mail é preciso: e-mail do destinatário, assunto e a mensagem.
    
### 4) pedido-api:
    - É o microsserviço central do projeto;
    - Capaz de efetuar, buscar e listar pedidos;
    - Para efetuar um Pedido é preciso: 
        - Id de Cliente válido cadastrado na api de clientes;
        - Lista de itens com:
            - Id do Produto válido cadastrado na api de catálogo;
            - Quantidade a ser comprada (deve existir essa quantidade em estoque).
    - Se todos os dados passados forem válidos um pedido é REALIZADO e será ENVIADO;
    - O estoque é atualizado através da api de catálogo;
    - Uma mensagem será enviada por e-mail (através da api de e-mail) após o pedido ser ENVIADO;
    - Caso aconteça um erro durante o procedimento o Status do Pedido mudará para ERRO_NO_PEDIDO.
    - Como padrão o projeto está sem o Circuit Breaker, para testá-lo basta descomentar as seguintes linhas e comentar as anteriores:
        - Na Classe PedidoService: linhas 79 e 88;
        - Na Classe PubSubListener: linha 114.

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






