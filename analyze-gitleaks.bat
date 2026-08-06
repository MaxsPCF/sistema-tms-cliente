@echo off
echo ============================================
echo    ANALISIS DE HALLAZGOS GITLEAKS
echo ============================================
echo.

echo [1/2] Generando reporte JSON...
gitleaks detect --config=.gitleaks.toml --report-format=json --report-path=gitleaks-report.json --verbose

if exist gitleaks-report.json (
    echo ✅ Reporte generado: gitleaks-report.json
    echo.
    echo [2/2] Mostrando resumen...
    
    REM Ejecutar Python para análisis detallado
    python -c "import json; data=json.load(open('gitleaks-report.json')); rules={}; [rules.update({f['RuleID']: rules.get(f['RuleID'], 0) + 1}) for f in data]; print('\n=== HALLAZGOS POR REGLA ==='); [print(f'  {c}x - {r}') for r,c in sorted(rules.items(), key=lambda x: -x[1])]; files=sorted(set(f['File'] for f in data)); print(f'\n=== ARCHIVOS AFECTADOS ==='); [print(f'  📄 {f}') for f in files]; print(f'\nTotal hallazgos: {len(data)}')"
    
    if errorlevel 1 (
        echo.
        echo ⚠️  Python no disponible. Mostrando resumen básico...
        echo.
        echo === REGLAS ACTIVADAS ===
        gitleaks detect --config=.gitleaks.toml --verbose 2>&1 | findstr /C:"Rule:" /C:"File:"
    )
) else (
    echo ❌ Error generando el reporte
)

echo.
echo ============================================
echo Para ver detalles: type gitleaks-report.json
echo ============================================
pause