# Copie suas 5 fotos para uma pasta e execute este script apontando para ela.
# Nomes esperados dos arquivos de origem (ajuste conforme necessário):
#   fiat-mobi.*, chevrolet-onix.*, vw-golf.*, ford-ka-branco.*, honda-civic.*
# Formatos: .jpg, .jpeg ou .png (o destino usa a mesma extensão do primeiro arquivo encontrado).

param(
    [Parameter(Mandatory = $true)]
    [string] $PastaOrigem
)

$destDir = Join-Path $PSScriptRoot "..\src\main\resources\static\img\cars" | Resolve-Path
$map = @(
    @{ key = "fiat-mobi"; patterns = @("fiat*mobi*", "mobi*") },
    @{ key = "chevrolet-onix"; patterns = @("chevrolet*onix*", "onix*") },
    @{ key = "vw-golf"; patterns = @("*golf*", "vw*golf*") },
    @{ key = "ford-ka-branco"; patterns = @("ford*ka*", "ka*branco*", "*ka*") },
    @{ key = "honda-civic"; patterns = @("honda*civic*", "civic*") }
)

foreach ($entry in $map) {
    $found = $null
    foreach ($pat in $entry.patterns) {
        $found = Get-ChildItem -Path $PastaOrigem -File -ErrorAction SilentlyContinue |
            Where-Object { $_.Name -like $pat -and $_.Extension -match '\.(jpg|jpeg|png)$' } |
            Select-Object -First 1
        if ($found) { break }
    }
    if (-not $found) {
        Write-Warning "Nenhum arquivo encontrado para $($entry.key) em $PastaOrigem"
        continue
    }
    $destName = "$($entry.key)$($found.Extension.ToLower())"
    $destPath = Join-Path $destDir $destName
    Copy-Item -LiteralPath $found.FullName -Destination $destPath -Force
    Write-Host "OK: $($found.Name) -> $destName"
}

Write-Host "`nAtualize imagem_url em data.sql se mudar a extensão (.png em vez de .jpg)."
