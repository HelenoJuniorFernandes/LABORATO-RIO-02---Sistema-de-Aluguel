@echo off
echo ========================================
echo    Sistema de Aluguel de Carros
echo ========================================
echo.

REM Verificar se Java está instalado
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado!
    echo Por favor, instale o Java 17 ou superior.
    echo Download: https://adoptium.net/
    pause
    exit /b 1
)

REM Verificar versão do Java
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%g
)

echo Java encontrado: %JAVA_VERSION%
echo.

REM ============================================================================
REM    INICIO DA SINCRONIZACAO DE ARQUIVOS VISUAIS
REM ============================================================================
echo Sincronizando as alteracoes visuais (HTML)...
xcopy "%~dp0LABORATO-RIO-02---Sistema-de-Aluguel\rental-system\src\main\resources\templates\*.html" "%~dp0rental-system\src\main\resources\templates\" /Y /I
echo.

echo Sincronizando imagens dos carros...
xcopy "%~dp0LABORATO-RIO-02---Sistema-de-Aluguel\rental-system\src\main\resources\static\img" "%~dp0rental-system\src\main\resources\static\img\" /Y /I /E
echo.

echo Sincronizando arquivo de deploy (render.yaml)...
xcopy "%~dp0LABORATO-RIO-02---Sistema-de-Aluguel\render.yaml" "%~dp0" /Y /I
echo.

echo ============================================================================
echo    SINCRONIZACAO CONCLUIDA
echo ============================================================================
echo.

REM Navegar para o diretório do projeto
cd /d "%~dp0rental-system"

REM Verificar se o Maven wrapper existe
if not exist "mvnw.cmd" (
    echo ERRO: Maven wrapper nao encontrado!
    echo Certifique-se de que esta executando o script na pasta correta.
    pause
    exit /b 1
)

echo Iniciando aplicacao Micronaut...
echo.
echo A aplicacao estara disponivel em: http://localhost:8082 (ou a porta que voce configurou)
echo.
echo Pressione Ctrl+C para parar a aplicacao
echo.

REM Executar a aplicação
mvnw.cmd mn:run

pause
