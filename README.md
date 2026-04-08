# 🏋️‍♂️ FitMedia - Microservices Fitness Platform

FitMedia é uma aplicação web baseada em microserviços que permite aos usuários registrar atividades físicas e receber recomendações inteligentes com base nos dados informados.

A plataforma combina **monitoramento de exercícios** com **análise por inteligência artificial**, fornecendo insights personalizados para melhorar desempenho, segurança e evolução nos treinos.

---

## 🚀 Funcionalidades

- 📊 Registro de atividades físicas:
  - Calorias gastas
  - Tempo de execução
  - Métricas adicionais personalizadas

- 🤖 Recomendações com Inteligência Artificial:
  - Sugestões de melhoria
  - Medidas de proteção contra lesões
  - Análise detalhada do desempenho

- 🔐 Autenticação e autorização com Keycloak

- 📡 Comunicação assíncrona entre serviços com RabbitMQ

- 🌐 Arquitetura escalável baseada em microserviços

---

## 🧱 Arquitetura do Sistema

O sistema é composto pelos seguintes microserviços:

- 👤 **User Service** → Gerenciamento de usuários
- 🏃 **Activity Service** → Registro de atividades físicas
- 🧠 **AI Service** → Geração de recomendações inteligentes
- ⚙️ **Config Server** → Centralização de configurações
- 🌍 **API Gateway** → Roteamento das requisições
- 🧭 **Eureka Server** → Service Discovery

---

## 🛠️ Tecnologias Utilizadas

- **Java 21 + Spring Boot**
- **Spring Cloud (Eureka, Config Server, Gateway)**
- **RabbitMQ**
- **Keycloak**
- **PostgreSQL** (usuários e recomendações)
- **MySQL** (atividades)
- **Maven**

---

## 🔄 Fluxo da Aplicação

1. O usuário se autentica via **Keycloak**
2. Registra uma atividade física
3. O **Activity Service** publica um evento no **RabbitMQ**
4. O **AI Service** consome o evento e processa os dados
5. Uma recomendação é gerada
6. O usuário recebe feedback detalhado sobre sua atividade

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos

- Java 21+
- Maven
- Docker (para RabbitMQ e Keycloak)
- PostgreSQL
- MySQL

---

### 🐳 Subindo dependências externas

#### RabbitMQ

```bash
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672
  rabbitmq:4-management
```
Painel disponível em: http://localhost:15672

Usuário padrão: guest / guest

#### Keycloak

```bash
docker run -p 8181:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e
  KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.5.7 start-dev
```
Painel disponível em: http://localhost:8181

Usuário padrão: admin / admin

---

### 🗄️ Configuração dos bancos de dados

Crie os bancos manualmente:

#### PostgreSQL
- Database: `fitmedia-user-db`
- Database: `fitmedia-recommendation-db`

#### MySQL
- Database: `fitmedia-activity-db`

---

### ▶️ Executando os serviços

Execute os serviços na seguinte ordem:

1. Config Server  
2. Eureka Server  
3. API Gateway  
4. User Service  
5. Activity Service  
6. AI Service  

Comando para execução de cada serviço:

```bash 
  mvn spring-boot:run  
```
Ou utilize sua IDE preferida.

---

## 🔐 Autenticação

O sistema utiliza o **Keycloak** para gerenciamento de identidade:

- OAuth2 / OpenID Connect  
- Proteção centralizada via API Gateway  
- Controle de acesso baseado em roles  

---

## 📬 Comunicação entre serviços

- **Síncrona:** REST APIs  
- **Assíncrona:** RabbitMQ (Event-Driven Architecture)  

---

## 📁 Estrutura do Projeto

fitmedia-microservice/

├── user-service  
├── activity-service  
├── ai-service  
├── api-gateway  
├── config-server  
├── eureka-server  

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## ⭐ Considerações Finais

O FitMedia demonstra na prática:

- Arquitetura de microserviços moderna  
- Uso de mensageria com RabbitMQ  
- Integração com Keycloak para segurança  
- Boas práticas com Spring Cloud  
- Separação de responsabilidades entre serviços  

---

## 👨‍💻 Autor

Desenvolvido por **Pedro Anjos** 🚀  
🔗 GitHub: https://github.com/pedroacbg  
