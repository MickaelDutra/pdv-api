# Plano inicial do backend do PDV

## 1. Objetivo

Definir as etapas iniciais para o desenvolvimento do backend do sistema PDV, alinhado à arquitetura descrita em arquitetura.md.

## 2. Premissas

- Backend em Java 21 + Spring Boot.
- Banco PostgreSQL com migrations via Flyway.
- Autenticação baseada em JWT.
- Arquitetura em camadas com módulos separados por domínio.
- Segurança, rastreabilidade e testes como requisitos principais desde o início.

## 3. Etapas de desenvolvimento

### Etapa 1 — Fundação do projeto

Objetivo: estruturar o projeto para crescer sem retrabalho.

Entregas:
- Configuração do projeto com Spring Boot e Maven.
- Configuração do PostgreSQL e Docker Compose.
- Estrutura inicial de pastas por módulo.
- Configuração do Flyway.
- Configuração do OpenAPI/Swagger.
- Logging e tratamento global de exceções.
- Configuração básica de testes unitários e de integração.

Exemplos de módulos iniciais:
- auth
- user
- product
- stock
- sale
- finance
- report

### Etapa 2 — Autenticação e autorização

Objetivo: habilitar acesso seguro ao sistema.

Entregas:
- Cadastro de usuários.
- Login com autenticação JWT.
- Controle de papéis e permissões (RBAC).
- Endpoint de recuperação de senha.
- Proteção de rotas por perfil.
- Auditoria básica de ações do usuário.

Perfis iniciais:
- Administrador
- Caixa
- Recebimento
- Gerente (futuro)

### Etapa 3 — Módulo de produtos

Objetivo: disponibilizar o cadastro e consulta de produtos.

Entregas:
- CRUD de produtos.
- Validação de campos obrigatórios.
- Controle de status do produto.
- Busca por nome, código interno e código de barras.
- Estrutura para preço, custo, marca, categoria e unidade.

### Etapa 4 — Módulo de estoque

Objetivo: controlar entradas, saídas, ajustes e movimentações.

Entregas:
- Cadastro de movimentações de estoque.
- Regras para entrada e saída.
- Atualização de saldo apenas por meio de movimentação.
- Histórico de estoque por produto.
- Inventário inicial.

Regras de negócio:
- Nunca alterar saldo diretamente.
- Toda alteração gera uma movimentação.
- O sistema deve registrar tipo, quantidade, usuário e data.

### Etapa 5 — Módulo de vendas

Objetivo: suportar o fluxo de vendas do ponto de venda.

Entregas:
- Criação de venda.
- Adição de itens ao carrinho.
- Cálculo de total.
- Seleção de forma de pagamento.
- Finalização da venda.
- Baixa de estoque ao confirmar venda.
- Geração de movimentação de saída.
- Registro de transações financeiras.

Considerações:
- A integração com máquina de cartão deve ser encapsulada em um serviço externo.
- O backend deve aceitar falhas parciais e registrar o estado da transação.

### Etapa 6 — Controle financeiro

Objetivo: registrar o impacto financeiro das vendas.

Entregas:
- Registro de valores de venda.
- Separação entre valor bruto, desconto, valor líquido e pagamento.
- Histórico financeiro por venda.
- Estrutura para futuras integrações com gateways de pagamento.

### Etapa 7 — Relatórios e consultas

Objetivo: fornecer dados para gestão.

Entregas:
- Relatório de vendas do dia.
- Relatório de faturamento.
- Relatório de produtos vendidos.
- Relatório de estoque baixo.
- Relatório de produtos sem movimentação.

### Etapa 8 — Segurança, observabilidade e deploy

Objetivo: preparar o sistema para uso real.

Entregas:
- Configuração de ambiente de produção.
- Health checks.
- Logs estruturados.
- Métricas básicas.
- Backup e política de retenção do banco.
- Deploy com Docker Compose e Nginx.

## 4. Ordem recomendada de implementação

1. Fundação do projeto
2. Autenticação e usuários
3. Produtos
4. Estoque
5. Vendas
6. Financeiro
7. Relatórios
8. Deploy e operação

## 5. Recomendação de arquitetura interna

Cada domínio deve seguir a estrutura abaixo:

- controller
- dto
- entity
- mapper
- repository
- service
- validator
- exception

## 6. Critérios de sucesso iniciais

- O sistema autentica usuários corretamente.
- É possível cadastrar produtos e controlar estoque.
- É possível realizar vendas com baixa de estoque.
- As permissões por perfil funcionam corretamente.
- O backend possui testes mínimos e documentação via Swagger.

## 7. Próximo passo

O próximo passo é iniciar a implementação da Etapa 1, criando a estrutura base do projeto Spring Boot com Docker, Flyway e configuração inicial de segurança.
