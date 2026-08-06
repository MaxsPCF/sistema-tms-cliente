# analyze-gitleaks.ps1 - Análisis de hallazgos Gitleaks
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   ANALISIS DE HALLAZGOS GITLEAKS" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que existe el config
if (-not (Test-Path ".gitleaks.toml")) {
    Write-Host "❌ No se encuentra .gitleaks.toml" -ForegroundColor Red
    exit 1
}

# Generar reporte JSON
Write-Host "[1/3] Generando reporte JSON..." -ForegroundColor Yellow
gitleaks detect --config=.gitleaks.toml --report-format=json --report-path=gitleaks-report.json 2>$null

if (Test-Path "gitleaks-report.json") {
    Write-Host "✅ Reporte generado" -ForegroundColor Green
    
    # Leer reporte
    $report = Get-Content "gitleaks-report.json" -Raw | ConvertFrom-Json
    
    if ($report.Count -gt 0) {
        Write-Host ""
        Write-Host "[2/3] Analizando hallazgos..." -ForegroundColor Yellow
        
        # Resumen por regla
        Write-Host "`n=== HALLAZGOS POR REGLA ===" -ForegroundColor Cyan
        $report | Group-Object RuleID | Sort-Object Count -Descending | ForEach-Object {
            $color = if($_.Count -ge 5){'Red'}elseif($_.Count -ge 3){'Yellow'}else{'White'}
            Write-Host "  $($_.Count)x - $($_.Name)" -ForegroundColor $color
        }
        
        # Archivos afectados
        Write-Host "`n=== ARCHIVOS AFECTADOS ===" -ForegroundColor Cyan
        $report | Select-Object -ExpandProperty File -Unique | Sort-Object | ForEach-Object {
            Write-Host "  📄 $_" -ForegroundColor White
        }
        
        # Top 5 hallazgos
        Write-Host "`n=== TOP 5 HALLAZGOS ===" -ForegroundColor Cyan
        $report | Select-Object -First 5 | ForEach-Object {
            Write-Host "  Regla: $($_.RuleID)" -ForegroundColor Magenta
            Write-Host "    Archivo: $($_.File)" -ForegroundColor White
            Write-Host "    Línea: $($_.StartLine)" -ForegroundColor Gray
            Write-Host "    Secret: $($_.Secret.Substring(0, [Math]::Min(20, $_.Secret.Length)))..." -ForegroundColor DarkGray
            Write-Host ""
        }
        
        Write-Host "Total hallazgos: $($report.Count)" -ForegroundColor Red
        
        # Guardar también en CSV para Excel
        Write-Host ""
        Write-Host "[3/3] Generando CSV para Excel..." -ForegroundColor Yellow
        gitleaks detect --config=.gitleaks.toml --report-format=csv --report-path=gitleaks-report.csv 2>$null
        Write-Host "✅ CSV generado: gitleaks-report.csv" -ForegroundColor Green
        
    } else {
        Write-Host "✅ No se encontraron hallazgos" -ForegroundColor Green
    }
} else {
    Write-Host "❌ Error generando el reporte" -ForegroundColor Red
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "Reportes generados:" -ForegroundColor White
Write-Host "  JSON: gitleaks-report.json" -ForegroundColor Gray
Write-Host "  CSV:  gitleaks-report.csv" -ForegroundColor Gray
Write-Host "============================================" -ForegroundColor Cyan