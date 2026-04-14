
# Sistema de Aluguel de Carros

![Java](https://img.shields.io/badge/Java-17+-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.5-success?style=for-the-badge&logo=spring)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?style=for-the-badge&logo=apache-maven)


Este projeto é um sistema web para apoiar a gestão de aluguéis de automóveis, permitindo que clientes solicitem aluguéis e que agentes da empresa avaliem e processem esses pedidos. O sistema foi desenvolvido como parte do Laboratório de Desenvolvimento de Software da PUC Minas.


## 📋 Funcionalidades

O protótipo atual implementa o fluxo principal do sistema, incluindo:

* **Autenticação e Autorização**: O sistema requer cadastro prévio e possui dois níveis de acesso distintos: **Cliente** e **Agente**.
* **Gestão de Pedidos pelo Cliente**: Usuários do tipo "Cliente" podem criar, modificar, consultar e cancelar seus pedidos de aluguel.
* **Avaliação de Pedidos pelo Agente**: Usuários do tipo "Agente" (representando empresas ou bancos) podem visualizar todos os pedidos pendentes e realizar a análise financeira para aprová-los ou rejeitá-los.
* **Persistência de Dados**: O sistema armazena informações sobre contratantes (clientes), automóveis e os próprios pedidos de aluguel.
* **Dashboard do Cliente**: Interface com menu lateral e área principal mostrando resumo completo dos pedidos e estatísticas.
* **Dashboard do Agente**: Interface para agentes com visão geral de todos os pedidos e ferramentas de análise.
* **Gestão de Veículos**: Sistema completo de cadastro, edição e exclusão de automóveis (apenas para agentes).

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias, conforme a especificação do projeto:

* **Backend**:
    * Java 17+
    * Spring Boot 3.1.5
    * Spring MVC
    * Spring Security (para autenticação e autorização)
    * Spring Data JPA / Hibernate (para persistência de dados)
* **Frontend**:
    * Thymeleaf (motor de templates para renderizar as páginas web)
    * CSS3 com tema claro corporativo e design responsivo
    * Interface profissional com cores neutras e elementos visuais limpos
* **Banco de Dados**:
    * H2 Database (banco de dados em memória para ambiente de desenvolvimento)
* **Build Tool**:
    * Maven

## 🚀 Como Executar o Projeto

Para instruções detalhadas sobre como executar o projeto, consulte o arquivo [COMO-EXECUTAR.md](COMO-EXECUTAR.md).

## 🏛️ Estrutura do Projeto

O código está organizado seguindo as camadas da arquitetura MVC, conforme a especificação do projeto:

* `com.pucminas.rental_system.config`: Configurações de segurança (Spring Security).
* `com.pucminas.rental_system.controller`: Classes que recebem as requisições web.
* `com.pucminas.rental_system.model`: Entidades JPA que representam os dados do sistema.
* `com.pucminas.rental_system.repository`: Interfaces do Spring Data JPA para acesso ao banco de dados.
* `com.pucminas.rental_system.service`: Classes que contêm a lógica de negócio do sistema.
