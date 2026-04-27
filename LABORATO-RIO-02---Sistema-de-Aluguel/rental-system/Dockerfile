# Estágio 1: Build com Maven e Java 17 (Builder)
FROM eclipse-temurin:17-jdk-jammy AS builder

# Define o diretório de trabalho
WORKDIR /app

# Otimização: Copia os arquivos de build e baixa as dependências.
# O contexto do Docker é a raiz do repositório, então precisamos especificar a subpasta 'rental-system'.
COPY rental-system/.mvn/ .mvn/
COPY rental-system/mvnw rental-system/pom.xml ./

# Dá permissão de execução ao wrapper
RUN chmod +x mvnw

# Baixa as dependências para aproveitar o cache
RUN ./mvnw dependency:go-offline

# Agora copia o resto do código fonte
COPY rental-system/src ./src

# Executa o comando do Maven para empacotar a aplicação, pulando os testes.
RUN ./mvnw package -DskipTests

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