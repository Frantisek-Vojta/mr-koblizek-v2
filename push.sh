#!/bin/bash

# Přejděte do správného adresáře
cd "/home/frantisek/Desktop/mr-koblizek-v2-main" || { echo "Adresář neexistuje!"; exit 1; }

# Zkontrolujeme, zda je Git nainstalovaný
if ! command -v git &> /dev/null
then
    echo "Git není nainstalován."
    exit 1
fi

# Zeptáme se na název pro commit
read -p "Zadejte název pro commit: " commitMessage

# Získání aktuálního data a času ve formátu: dd.MM.yyyy - HH:mm
datetime=$(date +"%d.%m.%Y - %H:%M")

# Nastavení commit zprávy s názvem commitMessage, datem a časem
finalCommitMessage="$commitMessage - $datetime"

# Stáhneme změny z origin/main
echo "Stahuji změny z origin/main..."
git pull origin main || { echo "Chyba při stahování změn."; exit 1; }

# Přidání změn do Gitu
git add . || { echo "Chyba při přidávání změn."; exit 1; }

# Commit s automaticky generovanou zprávou
git commit -m "$finalCommitMessage" || { echo "Chyba při commitování."; exit 1; }

# Pushnutí změn na origin/main
git push origin main || { echo "Chyba při pushování změn."; exit 1; }

# Úspěšné dokončení
echo "Commit a push byl úspěšně dokončen."

