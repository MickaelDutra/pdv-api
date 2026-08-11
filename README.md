# PDV API

API backend do sistema PDV construída com Spring Boot, PostgreSQL, Flyway, JWT e Docker.

## Visão geral

Este repositório contém o backend do sistema de ponto de venda (PDV), com funcionalidades de:

- autenticação e autorização via JWT
- cadastro e consulta de produtos
- movimentação de estoque
- processamento de vendas com cálculo de total e decremento de estoque
- documentação Swagger/OpenAPI
- deploy com Docker Compose

## Tecnologias

- Java 21
- Spring Boot 3.3.x
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker / Docker Compose

## Pré-requisitos

- JDK 21
- Maven 3.9+
- Docker
- Docker Compose

## Executar localmente

No diretório `api`:

```bash
cd api
mvn clean package
mvn spring-boot:run
```

## Executar com Docker Compose

No diretório `api`:

```bash
docker compose up --build
```

## Testes

No diretório `api`:

```bash
mvn test
```

## Endpoints principais

- `GET /api/health`
- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/products`
- `POST /api/products`
- `GET /api/stock-movements`
- `POST /api/stock-movements`
- `GET /api/sales`
- `POST /api/sales`

## Documentação e referências

- Swagger UI: `/swagger-ui.html`
- OpenAPI: `/v3/api-docs`
- Arquitetura: `api/arquitetura.md`
- Plano de backend: `api/backend-plano.md`

## Status atual

- Funcional: autenticação JWT, CRUD de produtos, controle de estoque, vendas com integração de estoque.
- Em desenvolvimento: relatórios financeiros e fluxo de caixa.
