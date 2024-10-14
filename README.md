# msbiblioteca
Implementação de uma arquitetura completa de microserviços para biblioteca digital

**Tecnologias utilizadas:** <br>

- Módulos Spring Cloud/Boot

- Service discovery Eureka

- Api Gateway

- Balanceamento de carga

- Comunicação síncrona com OpenFeign 

- Comunicação assícrona de microserviços com Serviço/Fila de mensageria com RabbitMQ

- Authorization Server
  
- Resource Server
    
- Desenvolvimento de Imagens Docker

**Banco de dados** <br>
*MySQL*

## Documentação API
http://www.meudominio.com.br/api/v1/swagger-ui/index.html

**Comandos docker** <br>

<ins>Criar imagem a partir do dockerfile</ins>

docker build --tag msleitor:1.0.0 .

docker build --tag msestante:1.0.0 .

docker build --tag msautenticacao:1.0.0 .

docker build --tag mseureka:1.0.0 .

docker build --tag msgateway:1.0.0 .

<ins>Criar network</ins> <br>

docker network create msnetwork

<ins>Executar container</ins>

docker run --name mseureka -p 8761:8761 --network msnetwork mseureka:1.0.0

docker run --name msleitor --network msnetwork msleitor:1.0.0

docker run --name msestante --network msnetwork msestante:1.0.0

docker run --name msautenticacao -p 8085:8085 --network msnetwork msautenticacao:1.0.0

docker run --name msgateway -p 8080:8080 --network msnetwork msgateway:1.0.0

docker run -it --network msnetwork  --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management
