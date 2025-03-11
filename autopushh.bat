# Přejdi do adresáře s git repozitářem
Set-Location -Path "C:\Users\xCel_\Desktop\mr-koblih-v5312789"

# Požádej uživatele o zadání zprávy pro commit
$commitMessage = Read-Host "Zadejte název pro commit"

# Získání aktuálního data a času
$currentDateTime = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"

# Kombinuj název commit zprávy a aktuální datum a čas
$finalCommitMessage = "$commitMessage - $currentDateTime"

# Přidej změny do Gitu
git add .

# Commitni změny s definovanou zprávou
git commit -m "$finalCommitMessage"

# Pushni změny do origin/main
git push origin main

Write-Host "Commit a push byl úspěšně dokončen."

# Po dokončení čekej na stisknutí Enter dvakrát
Write-Host "Stiskněte Enter pro ukončení..."
$null = Read-Host
$null = Read-Host
