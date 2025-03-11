@echo off
cd /d "C:\Users\xCel_\Desktop\mr-koblih-v5312789"

:: Zkontroluj vzdálený repozitář
echo Kontroluji vzdálený repozitář...
git remote -v

:: Zkontroluj aktuální větev
echo Kontroluji aktuální větev...
git branch

:: Zkontroluj změny v repozitáři
echo Zkontroluji změny v repozitáři...
git status

:: Pokud jsou nějaké změny, požádej o commit zprávu
if not exist ".git" (
    echo Tento adresar neni git repozitar!
    pause
    exit /b
)

set /p commitMessage="Zadejte název pro commit: "

:: Získání aktuálního data a času pro commit zprávu
for /f "tokens=1-4 delims=/- " %%a in ("%date%") do set currentDateTime=%%a-%%b-%%c_%%d

:: Spojení commit zprávy s aktuálním datem a časem
set finalCommitMessage=%commitMessage% - %currentDateTime%

:: Přidání změn do Git a commit
echo Přidávám změny do Gitu...
git add .
git commit -m "%finalCommitMessage%"

:: Kontrola, zda commit byl úspěšný
if %errorlevel% neq 0 (
    echo Commit selhal! Zkontrolujte chyby.
    pause
    exit /b
)

:: Pushnutí změn do vzdáleného repozitáře
echo Pushuji změny do origin/main...
git push origin main

:: Zkontroluj, zda push proběhl úspěšně
if %errorlevel% neq 0 (
    echo Push selhal! Zkontrolujte chyby.
    pause
    exit /b
)

:: Úspěšné dokončení
echo Commit a push byl úspěšně dokončen.
pause
