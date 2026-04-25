# 🚗 Sistema de Aluguel de Carros - Como Executar

## 📋 Pré-requisitos

### Java
- **Java 17 ou superior** é obrigatório

### Verificar se Java está instalado:
```bash
java -version
```

## 🖥️ Windows

### Método 1: Script Automático (Recomendado)
```bash
# Clique duplo no arquivo ou execute no terminal:
run-windows.bat
```

### Método 2: Manual
```cmd
# Após baixar e extrair o projeto:
# 1. Abrir PowerShell/CMD como Administrador
# 2. Navegar para a pasta do projeto
cd "C:\caminho\para\LAB02-SistemaAluguelDeCarros"

# 3. Entrar na pasta rental-system
cd rental-system

# 4. Executar a aplicação
.\mvnw.cmd mn:run
```

## 🐧 Linux / 🍎 macOS

### Método 1: Script Automático (Recomendado)
```bash
# Dar permissão de execução (apenas na primeira vez)
chmod +x run-linux-mac.sh

# Executar o script
./run-linux-mac.sh
```

### Método 2: Manual
```bash
# 1. Abrir terminal
# 2. Navegar para a pasta do projeto
cd /caminho/para/LAB02-SistemaAluguelDeCarros

# 3. Entrar na pasta rental-system
cd rental-system

# 4. Dar permissão ao Maven wrapper
chmod +x mvnw

# 5. Executar a aplicação
./mvnw spring-boot:run
```

## 🌐 Acessar a Aplicação

Após iniciar, acesse: **http://localhost:8080**

### Credenciais de Teste:
- **Cliente**: cliente@email.com / password
- **Agente**: agente@email.com / password

### Acesso ao Banco de Dados H2
Para inspecionar os dados diretamente:
* **URL**: `http://localhost:8080/h2-console`
* **JDBC URL**: `jdbc:h2:mem:rentalsystemdb`
* **User Name**: `sa`
* **Password**: `password`

## 📁 Estrutura do Projeto
```
LAB02-SistemaAluguelDeCarros/
├── run-windows.bat          # Script para Windows
├── run-linux-mac.sh         # Script para Linux/macOS
├── COMO-EXECUTAR.md         # Este arquivo
└── rental-system/           # Projeto Spring Boot
    ├── mvnw                 # Maven wrapper (Linux/macOS)
    ├── mvnw.cmd             # Maven wrapper (Windows)
    └── pom.xml              # Configuração Maven
```

## 🔧 Solução de Problemas

### Erro: "Java não encontrado"
- Instale Java 17+
- Verifique se JAVA_HOME está configurado

### Erro: "Maven wrapper não encontrado"
- Certifique-se de estar na pasta correta
- Execute os scripts do diretório raiz do projeto

### Erro: "Porta 8080 já está em uso"
- Pare outras aplicações na porta 8080
- Ou altere a porta em `application.properties`:
  ```properties
  server.port=8081
  ```

### Erro de versão do Java
- Spring Boot 3.1.5 precisa Java 17+
- Verifique: `java -version`
- Configure JAVA_HOME se necessário


## 💡 Dicas

1. **Use os scripts automáticos** - eles verificam tudo automaticamente
2. **Mantenha Java atualizado** - sempre use Java 17 ou superior
3. **Execute como Administrador** no Windows se necessário
4. **Verifique a porta 8080** - não deve estar em uso por outras aplicações