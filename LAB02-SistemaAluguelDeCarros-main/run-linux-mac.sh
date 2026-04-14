#!/bin/bash

echo "========================================"
echo "    Sistema de Aluguel de Carros"
echo "========================================"
echo

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "ERRO: Java não encontrado!"
    echo "Por favor, instale o Java 17 ou superior."
    echo "Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "macOS: brew install openjdk@17"
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1)
echo "Java encontrado: $JAVA_VERSION"
echo

# Navegar para o diretório do projeto
cd "$(dirname "$0")/rental-system"

# Verificar se o Maven wrapper existe
if [ ! -f "mvnw" ]; then
    echo "ERRO: Maven wrapper não encontrado!"
    echo "Certifique-se de que está executando o script na pasta correta."
    exit 1
fi

# Tornar o Maven wrapper executável
chmod +x mvnw

echo "Iniciando aplicação Spring Boot..."
echo
echo "A aplicação estará disponível em: http://localhost:8080"
echo
echo "Pressione Ctrl+C para parar a aplicação"
echo

# Executar a aplicação
./mvnw spring-boot:run
