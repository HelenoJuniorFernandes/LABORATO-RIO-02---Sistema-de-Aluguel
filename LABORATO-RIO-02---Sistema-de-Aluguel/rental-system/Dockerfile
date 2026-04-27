# Estágio 1: Build com Maven e Java 17 (Builder)
# Usamos uma imagem completa do Maven para compilar o projeto.
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho
WORKDIR /app

# Otimização: Copia o pom.xml e baixa as dependências primeiro.
# Isso aproveita o cache do Docker. As dependências só serão baixadas novamente
# se o pom.xml for alterado.
COPY pom.xml .
RUN mvn dependency:go-offline

# Agora copia o resto do código fonte
COPY src ./src

# Executa o comando do Maven para empacotar a aplicação, pulando os testes.
RUN mvn package -DskipTests

# Estágio 2: Imagem final, leve e otimizada para produção
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copia APENAS o arquivo .jar gerado no estágio anterior.
COPY --from=builder /app/target/rental-system-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão que o Render usa. Sua aplicação já está configurada
# para usar a variável de ambiente PORT, então isso é mais para documentação.
EXPOSE 10000

# Comando para iniciar a aplicação quando o contêiner rodar.
ENTRYPOINT ["java", "-jar", "app.jar"]