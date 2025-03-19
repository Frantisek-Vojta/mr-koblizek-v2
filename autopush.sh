#!/bin/bash

# Přejdeme do správného adresáře, kde je repozitář
cd /home/frantisek/Documents/mr-koblizek-v2-main || { echo "Adresář neexistuje!"; exit 1; }

# Zeptáme se na název pro commit
read -p "Zadejte název pro commit: " commitMessage

# Získání aktuálního data a času pro commit zprávu (ve formátu 11.03.2025 - 14:30)
datetime=$(date +"%d.%m.%Y - %H:%M")

# Nastavení commit zprávy s názvem commitMessage, datem a časem
finalCommitMessage="${commitMessage} - ${datetime}"

# Stáhneme změny z origin/main
echo "Stahuji změny z origin/main..."
git pull origin main

# Přidání změn do Gitu
git add .

# Commit s automaticky generovanou zprávou
git commit -m "$finalCommitMessage"

# Pushnutí změn na origin/main
git push origin main

# Úspěšné dokončení
echo "Commit a push byl úspěšně dokončen."

