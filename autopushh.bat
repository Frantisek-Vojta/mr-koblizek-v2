Set-Location -Path "C:\Users\xCel_\Desktop\mr-koblih-v5312789"

$commitMessage = Read-Host "Zadejte název pro commit"

$currentDateTime = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"

$finalCommitMessage = "$commitMessage - $currentDateTime"

git add .

git commit -m "$finalCommitMessage"

git push origin main

Write-Host "Commit a push byl úspěšně dokončen."

Write-Host "Stiskněte Enter pro ukončení..."
$null = Read-Host
$null = Read-Host
