# Estágio 1: Build com Maven e Java 17 (Builder)
# Usamos uma imagem completa do Maven para compilar o projeto.
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app
# Copia todo o código do projeto para dentro do contêiner
COPY . .
# Executa o comando do Maven para empacotar a aplicação, pulando os testes.
RUN mvn package -DskipTests

# Estágio 2: Imagem final, leve e otimizada para produção
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copia APENAS o arquivo .jar gerado no estágio anterior.
COPY --from=builder /app/target/rental-system-0.0.1-SNAPSHOT.jar .
# Comando para iniciar a aplicação quando o contêiner rodar.
CMD ["java", "-jar", "rental-system-0.0.1-SNAPSHOT.jar"]