#!/bin/bash

echo "============================================"
echo "   ANALISIS DE HALLAZGOS GITLEAKS"
echo "============================================"
echo ""

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Generar reporte JSON
echo -e "${YELLOW}[1/3] Generando reporte JSON...${NC}"
gitleaks detect --config=.gitleaks.toml --format=json --report=gitleaks-report.json 2>/dev/null

if [ -f "gitleaks-report.json" ]; then
    echo -e "${GREEN}✅ Reporte generado: gitleaks-report.json${NC}"
    
    # Verificar si python3 está disponible
    if command -v python3 &> /dev/null; then
        echo ""
        echo -e "${YELLOW}[2/3] Analizando hallazgos...${NC}"
        
        python3 << 'PYEOF'
import json

with open('gitleaks-report.json') as f:
    data = json.load(f)

if len(data) == 0:
    print("\n✅ No se encontraron hallazgos")
else:
    # Agrupar por regla
    rules = {}
    files = set()
    for finding in data:
        rule = finding['RuleID']
        rules[rule] = rules.get(rule, 0) + 1
        files.add(finding['File'])
    
    print("\n=== HALLAZGOS POR REGLA ===")
    for rule, count in sorted(rules.items(), key=lambda x: -x[1]):
        print(f"  {count}x - {rule}")
    
    print(f"\n=== ARCHIVOS AFECTADOS ===")
    for f in sorted(files):
        print(f"  📄 {f}")
    
    print(f"\n=== DETALLES ===")
    print(f"Total hallazgos: {len(data)}")
    
    for finding in data:
        print(f"\n  Regla: {finding['RuleID']}")
        print(f"  Archivo: {finding['File']}")
        print(f"  Línea: {finding.get('StartLine', 'N/A')}")
        print(f"  Commit: {finding['Commit'][:8]}")
PYEOF
    else
        echo ""
        echo -e "${RED}❌ Python3 no encontrado. Instálalo para análisis detallado.${NC}"
        echo "   Reporte JSON disponible en: gitleaks-report.json"
    fi
else
    echo -e "${RED}❌ Error generando el reporte${NC}"
fi

echo ""
echo -e "${YELLOW}[3/3] Abrir Security tab en GitHub...${NC}"
REPO_URL=$(git config --get remote.origin.url | sed 's/\.git$//')
echo "👉 $REPO_URL/security"
echo ""