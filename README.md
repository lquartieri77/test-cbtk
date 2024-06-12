Projeto de teste para CbyK - por Leonardo Quartieri

# Visão geral

O projeto é uma aplicação back-end com objetivo de disponibilizar uma APIs utilizando os recursos Spring Boot, Postgres, Docker e docker-compose


# Setup da aplicação (local)

## Instalação da aplicação

Primeiramente, faça o clone do repositório:
```
https://github.com/emmanuelneri/productivity-with-spring.git
```
Feito isso, acesse o projeto:
```
cd api
```
Neste momento é preciso compilar o código e baixar as dependências do projeto:
```
mvn clean install
```
Finalizado esse passo, vamos iniciar a aplicação:
```
mvn spring-boot:run
```
Pronto. A aplicação está disponível em http://localhost:8080 (A aplicação esta segura por uma basic autentication, aqui estão os dados de auth: user= user, password= password) Junto ao codigo fonte da aplicação deixei também uma collection do postman para simplificar os testes.
```
Tomcat started on port(s): 8080 (http)
Started AppConfig in xxxx seconds (JVM running for xxxx)
```
Swagger da aplicação pode ser consultado em:
http://localhost:8080/swagger-ui/index.html#/

Java melody da aplicação pode ser consultado em:
http://localhost:8080/monitoring


# Setup da aplicação com docker

## Pré-requisito

Antes de rodar a aplicação é preciso garantir que as seguintes dependências estejam corretamente instaladas:

```
Java 17
Docker 17.06.0 
Maven 3.3.3 
```

## Preparando ambiente

Subir um container com o postgres 
```
docker run -p 5432:5432 -e POSTGRES_PASSWORD=1234 postgres
```

Criar nosso jar (neste ponto a aplicacao gera o jar, sobe, conecta no banco e roda os testes)
```
mvn clean install
```
Depois disso já teremos nosso jar criado, assim podemos derrubar o container para subir tudo, agora via compose

Temos um Dockerfile na pasta raiz da aplicacao, que esta sendo usado pelo compose para subir nossa aplicacao, assim basta subir com o seguite comando:
```
docker-compose up
```