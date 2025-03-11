@echo off
cd /d "C:\Users\xCel_\Desktop\mr-koblih-v5312789"

:: Získání aktuálního data a času pro commit zprávu
for /f "tokens=1-4 delims=/- " %%a in ("%date%") do set currentDateTime=%%a-%%b-%%c_%%d

:: Nastavení commit zprávy s názvem "commit" a aktuálním datem a časem
set finalCommitMessage=commit - %currentDateTime%

:: Stáhne změny z origin/main
echo Stahuji změny z origin/main...
git pull origin main

:: Přidání změn do Gitu
git add .

:: Commit s automaticky generovanou zprávou
git commit -m "%finalCommitMessage%"

:: Pushnutí změn na origin/main
git push origin main

:: Úspěšné dokončení
echo Commit a push byl úspěšně dokončen.
pause
