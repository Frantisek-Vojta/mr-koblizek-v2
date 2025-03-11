@echo off
cd /d "C:\Users\xCel_\Desktop\mr-koblih-v5312789"

set /p commitMessage="Zadejte název pro commit: "

for /f "tokens=1-4 delims=/- " %%a in ("%date%") do set currentDateTime=%%a-%%b-%%c_%%d

set finalCommitMessage=%commitMessage% - %currentDateTime%

git add .
git commit -m "%finalCommitMessage%"
git push origin main

echo Commit a push byl úspěšně dokončen.
pause
