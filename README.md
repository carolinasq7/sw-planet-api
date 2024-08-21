# SWPlanetAPI

SWPlanetAPI é uma API desenvolvida em Kotlin com Spring Boot para gerenciamento de planetas.
Este projeto permite criar, consultar, atualizar e deletar informações sobre planetas, como nome, clima e terreno.

No geral, esse projeto foi construído para exercitar meus conhecimentos em kotlin e aprimorar minhas habilidades com TESTES.

## Funcionalidades

- **Criação de Planetas**: Adicione novos planetas com informações como nome, clima e terreno.
- **Consulta de Planetas**: Consulte planetas existentes por ID ou lista todos os planetas com opções de paginação.
- **Deleção de Planetas**: Remova planetas do sistema.

## Tecnologias Utilizadas

- **Kotlin**: Linguagem de programação principal do projeto.
- **Spring Boot**: Framework para desenvolvimento da API.
- **JUnit 5**: Framework para testes.
- **Gradle**: Gerenciador de build.

## Configuração do Projeto

### Pré-requisitos

Certifique-se de ter o Java JDK 11 ou superior instalados em seu sistema e o MySQL configurado.

### Clonar o Repositório

Clone o repositório para seu ambiente local:

```git clone https://github.com/carolinasq7/sw-planet-api.git ```
```cd sw-planet-api```

### Configuração do Ambiente

Configuração do Banco de Dados: O projeto utiliza um banco de dados MySQL
Configuração do Projeto: Configure o ```application.yml``` com as credenciais do banco de dados e outras propriedades necessárias.

```sh
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/planet_db
    username: root
    password: root
  jpa:
    hibernate:
    ddl-auto: update
  show-sql: true
```

### Executar o Projeto

Para iniciar o aplicativo, use o comando Gradle:

```./gradlew bootRun```

### Executar Testes

Para executar os testes, use o comando:

```./gradlew test```

Caso queira rodar apenas os testes e2e, execute:
(para rodar os demais perfis, basta conferir os perfis criados no arquivo build.gradle)

```./gradlew e2eTest```

### Contato
Para mais informações ou perguntas, você pode entrar em contato através do [LinkedIn](https://www.linkedin.com/in/carolinalvess/)
