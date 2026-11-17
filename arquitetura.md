# System Design - PDV Web
**Versão:** 1.0  
**Autor:** Mickael Dutra  
**Objetivo:** Planejamento inicial da arquitetura do sistema.

---

# 1. Objetivo do Projeto

Desenvolver um sistema de Ponto de Venda (PDV) totalmente web, hospedado em uma VPS, permitindo que usuários autorizados acessem o sistema de qualquer computador da empresa.

O sistema deverá controlar:

- Cadastro de produtos
- Controle de estoque
- Processo de vendas
- Controle financeiro das vendas
- Controle de usuários
- Níveis de acesso
- Integração com máquina de cartão
- Emissão futura de NFC-e
- Relatórios

---

# 2. Stack Tecnológica

## Backend

- Java 21 (LTS)
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway (migração do banco)
- JWT Authentication
- Maven
- Hibernate
- Bean Validation
- OpenAPI (Swagger)

---

## Frontend

- Angular 20+
- Angular Material
- RxJS
- NgRx (caso necessário)
- SCSS
- JWT Authentication
- ngx-mask
- ngx-toastr

---

## Banco de Dados

PostgreSQL

Motivos:

- Gratuito
- Robusto
- Excelente performance
- Fácil backup
- Muito utilizado no mercado

---

## Hospedagem

Hostinger VPS

Sistema Operacional:

Ubuntu Server 24.04 LTS

---

## Servidor Web

Nginx

Responsável por:

- HTTPS
- Proxy reverso
- Compressão
- Cache
- Servir Angular

---

## Containerização

Docker

Docker Compose

Permitirá subir:

- Backend
- Banco
- Frontend
- Nginx

---

# 3. Arquitetura Geral

```text
                 Internet
                     │
              HTTPS (443)
                     │
                NGINX
          Reverse Proxy
         ┌────────┴────────┐
         │                 │
 Angular SPA         Spring Boot API
         │                 │
         └────────┬────────┘
                  │
             PostgreSQL
```

---

# 4. Arquitetura Backend

Adotar arquitetura em camadas.

```text
Controller
      │
Service
      │
Repository
      │
Database
```

Cada módulo possuirá sua própria estrutura.

Exemplo:

```text
produto

├── controller
├── dto
├── entity
├── mapper
├── repository
├── service
├── validator
└── exception
```

---

# 5. Arquitetura Frontend

```text
src

├── auth
├── core
├── shared
├── pages
│
├── dashboard
├── produtos
├── estoque
├── vendas
├── usuarios
├── relatorios
└── configuracoes
```

---

# 6. Módulos do Sistema

## Login

Responsável por:

- autenticação
- autorização
- recuperação de senha
- JWT

---

## Dashboard

Indicadores:

- vendas do dia
- faturamento
- produtos vendidos
- estoque baixo
- vendas por hora
- últimas vendas

---

## Produtos

Cadastro

Campos:

- Nome
- Descrição
- Código interno
- Código de barras
- Unidade
- Quantidade por caixa
- Peso
- Marca
- Categoria
- Custo
- Preço de venda
- Status

---

## Estoque

Responsável por:

Entrada

Saída

Ajuste

Inventário

Movimentações

---

## Vendas

Fluxo

```text
Selecionar produto

↓

Adicionar ao carrinho

↓

Calcular total

↓

Selecionar forma de pagamento

↓

Enviar para máquina de cartão (quando necessário)

↓

Receber confirmação

↓

Finalizar venda

↓

Baixar estoque

↓

Registrar movimentação
```

---

## Usuários

Cadastro

Perfis:

Administrador

Caixa

Recebimento

Gerente (futuro)

---

## Relatórios

- Produtos vendidos
- Estoque
- Lucro
- Faturamento
- Produtos sem movimentação
- Inventário

---

# 7. Controle de Estoque

Cada movimentação será registrada.

Tipos:

```text
ENTRADA

SAÍDA

VENDA

DEVOLUÇÃO

AJUSTE
```

Nunca alterar diretamente a quantidade.

Toda alteração gera uma movimentação.

---

# 8. Controle de Permissões

Utilizar RBAC (Role Based Access Control).

Exemplo:

Administrador

- tudo

Caixa

- venda
- consulta produto

Recebimento

- entrada estoque
- consulta estoque

Gerente

- relatórios
- vendas
- estoque

---

# 9. Fluxo de Venda

```text
Cliente

↓

Operador

↓

Pesquisa produto

↓

Carrinho

↓

Pagamento

↓

Confirmação

↓

Emissão comprovante

↓

Baixa estoque

↓

Fim
```

---

# 10. Integração com Máquina de Cartão

Fluxo esperado

```text
PDV

↓

API Integradora

↓

Máquina de cartão

↓

Cliente insere cartão

↓

Pagamento

↓

Resposta

↓

PDV recebe confirmação

↓

Finaliza venda
```

Inicialmente a integração ficará desacoplada para facilitar a troca de adquirente (Stone, Cielo, Rede, PagBank, etc.).

Será criada uma interface no backend para abstrair o provedor de pagamento.

Exemplo:

```java
PaymentTerminalService

StoneTerminalService

CieloTerminalService

RedeTerminalService
```

Assim, novas integrações poderão ser adicionadas sem alterar o fluxo principal de vendas.

---

# 11. Segurança

- JWT
- HTTPS
- BCrypt
- Spring Security
- Validação de entrada
- Auditoria
- Logs

---

# 12. Banco de Dados (Primeira Versão)

Principais tabelas

```text
usuarios

roles

usuario_roles

produtos

categorias

estoque

movimentacao_estoque

vendas

itens_venda

formas_pagamento

pagamentos

clientes (futuro)

fornecedores (futuro)
```

---

# 13. Fluxo Geral do Sistema

```text
Usuário

↓

Login

↓

Dashboard

↓

Produto

↓

Estoque

↓

Venda

↓

Pagamento

↓

Baixa Estoque

↓

Relatórios
```

---

# 14. Ferramentas Utilizadas

## Desenvolvimento

- IntelliJ IDEA
- VS Code
- Postman
- DBeaver
- Docker Desktop
- Git
- GitHub

---

## Banco

PostgreSQL

---

## Backend

- Spring Boot
- Spring Security
- Spring Data
- Flyway
- Swagger

---

## Frontend

- Angular
- Angular Material
- SCSS
- RxJS

---

## Deploy

- Ubuntu
- Docker
- Docker Compose
- Nginx
- Certbot (SSL)

---

# 15. Estrutura Inicial do Repositório

```text
pdv-system/

backend/

frontend/

database/

docker/

docs/

README.md

docker-compose.yml
```

---

# 16. Evoluções Futuras

- NFC-e
- PIX
- Cadastro de clientes
- Cadastro de fornecedores
- Compras
- Integração com balança
- Impressão de etiquetas
- Impressão de cupom
- Dashboard em tempo real
- Aplicativo mobile
- API pública
- Integração com ERP
- Backup automático
- Multiempresa (SaaS)
- Multi-filial
- Controle de caixa (abertura, sangria e fechamento)
- Promoções e descontos
- Programa de fidelidade

---

# 17. Arquitetura longo prazo

```text
                   Internet
                       │
                   HTTPS
                       │
                    NGINX
                       │
          ┌────────────┴────────────┐
          │                         │
     Angular SPA             Spring Boot API
                                      │
         ┌────────────────────────────┼────────────────────────────┐
         │                            │                            │
    Autenticação                Produtos                  Estoque
         │                            │                            │
         ├──────────────┬─────────────┴──────────────┬─────────────┤
                        │                            │
                    Vendas                    Pagamentos
                        │                            │
                        └──────────────┬─────────────┘
                                       │
                                  PostgreSQL
                                       │
                                 Backup Diário
```

---

# 18. Princípios Arquiteturais

- Clean Code
- SOLID
- Clean Architecture (adaptada)
- Separação de responsabilidades
- APIs REST
- Versionamento de banco com Flyway
- Autenticação stateless com JWT
- Integração desacoplada com adquirentes de pagamento
- Logs centralizados
- Observabilidade preparada para crescimento
- Estrutura preparada para futura migração para microsserviços, caso o volume do sistema justifique.

---