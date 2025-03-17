#!/bin/bash

# Nastavte cestu k vaší složce
REPO_PATH="/home/frantisek/Documents/mr-koblizek-v2-main"

# Nastavte GitHub URL repozitáře (přizpůsobte podle potřeby)
GITHUB_URL="https://github.com/Frantisek-Vojta/mr-koblizek-v2.git"

# Přepněte se do složky s projektem
cd $REPO_PATH || { echo "Složka nebyla nalezena."; exit 1; }

# Zkontrolujte, zda je v adresáři Git repozitář
if [ ! -d ".git" ]; then
    echo "Tento adresář není git repozitář. Inicializuji repozitář..."
    git init
    git remote add origin $GITHUB_URL
fi

# Zeptejte se uživatele na commit zprávu
read -p "Zadejte zprávu pro commit: " custom_message

# Získání aktuálního data a času pro commit message
commit_message="$custom_message $(date '+%d.%m.%Y %H:%M')"

# Přidání změn do staging area
git add .

# Vytvoření commitu
git commit -m "$commit_message"

# Push změn na GitHub
git push origin master || git push -u origin master
