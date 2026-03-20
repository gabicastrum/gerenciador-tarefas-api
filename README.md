# API de Gerenciamento de Tarefas

> Desafio de CRUD para gerenciar tarefas em um to-do list e consolidar conhecimentos nas stack utilizada para desenvolvimento.

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| ☕ **Java 21** | Linguagem principal para construção da API |
| 🍃 **Spring Boot 3** | Framework para criação de aplicações Java |
| 🐘 **Gradle** | Gerenciamento de dependências e build |
| 🗄️ **Spring Data JPA** | Persistência e acesso a dados |
| 💾 **H2 Database** | Banco de dados em memória para desenvolvimento |
| ✅ **Bean Validation** | Validação de dados de entrada |
| 🔁 **MapStruct** | Mapeamento entre entidades e DTOs |
| 📄 **SpringDoc OpenAPI** | Documentação interativa da API (Swagger UI) |
| 🧪 **JUnit 5** | Framework de testes unitários |
| 📊 **JaCoCo** | Relatório de cobertura de testes |

---

## 📡 Endpoints Implementados

| Método | Endpoint | Descrição                                                                 |
|---|---|---------------------------------------------------------------------------|
| `GET` | `/tarefas` | Lista todas as tarefas (com filtro opcional por status e paginação)       |
| `GET` | `/tarefas/{id}` | Retorna os detalhes de uma tarefa específica                              |
| `POST` | `/tarefas` | Cria uma nova tarefa                                                      |
| `PATCH` | `/tarefas/{id}` | Atualiza os dados (Título, Descrição e/ou Status) de uma tarefa existente |
| `DELETE` | `/tarefas/{id}` | Remove uma tarefa                                                         |

### 🔍 Filtro por status

O endpoint `GET /tarefas` aceita um parâmetro opcional `status` para filtrar as tarefas:

```
GET /tarefas?status=PENDENTE
GET /tarefas?status=CONCLUIDA
GET /tarefas           # lista todas
```

---

## 📄 Documentação da API (Swagger)

Com o projeto rodando, acesse a interface interativa do Swagger para testar os endpoints:

```
http://localhost:8080/swagger-ui/index.html
```

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos

- Java 21 instalado
- Gradle (ou usar o wrapper `./gradlew`)

### Instalação

```bash
# Clone o repositório
git clone https://github.com/gabicastrum/gerenciador-tarefas-api.git

# Acesse a pasta do projeto
cd tarefas
```

### Scripts Disponíveis

```bash
# Inicia o servidor de desenvolvimento
./gradlew bootRun

# Executa os testes
./gradlew test

# Gera o relatório de cobertura de testes (JaCoCo)
./gradlew test jacocoTestReport
```

### 📊 Relatório de Cobertura (JaCoCo)

Após rodar `./gradlew test`, o relatório estará disponível em:

```
build/reports/jacoco/test/html/index.html
```

Abra o arquivo `index.html` no navegador para visualizar a cobertura de testes (existe um atalho no Intellij: Botão direito -> Open In -> Browser).

---

<p align="center">Feito com apoio de ☕ por <strong>👩‍💻 Gabriela de Castro Laurindo</strong></p>