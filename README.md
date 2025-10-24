# mr. Koblizek v2

#### Moderní Discord bot vytvořený v Javě pomocí knihovny JDA (Java Discord API) s ekonomickým systémem a užitečnými funkcemi.

## 📋 Požadavky

- Java: Azul Zulu Community 21 nebo novější
- Maven: Pro správu závislostí
- Discord účet: Pro vytvoření bot aplikace

## 🚀 Instalace a spuštění
### 1. Naklonování a příprava

`git clone [odkaz-na-repozitar]`

`cd ...`

### 2. Konfigurace tokenu

- Otevřete soubor: `src/main/java/org/example/Main.java`

- Na řádku 21 nahraďte `TOKEN` vaším skutečným Discord bot tokenem:


### 3. Konfigurace kanálu pro role

- Otevřete soubor: `src/main/java/org/example/commands/RoleUpdate.java`
- Najděte konstantu a nahraďte ID kanálu:
  `private static final long LOG_CHANNEL_ID = 123456789012345678L;`

### 4. Sestavení a spuštění
- mvn clean compile
- mvn exec:java -Dexec.mainClass="org.example.Main"

## ⚙️ Konfigurace Discord bota
- ⚠️ Bot musi mit admin prava ⚠️

## 🎯 Dostupné příkazy
#### Základní příkazy

-    /botinfo - Informace o botovi

-   /donut - Získejte donut! 🍩

-  /freenitro - Žertovný příkaz

- /help - Nápověda a seznam příkazů

- /meme - Náhodný meme

- /ping - Otestujte odezvu bota

#### Ekonomický systém (/e ...)

-  /e balance - Zobrazí váš zůstatek

- /e baltop - Žebříček nejbohatších uživatelů

- /e help - Nápověda ekonomického systému

-  /e profile - Váš ekonomický profil

- /e slots - Hra na výherních automatech

- /e work - Pracujte a vydělávejte peníze

## 🚧 Neaktivní příkazy 🚧

#### Následující příkazy jsou momentálně nefunkční:
- guess - Hádani cisla
- love - Kalkulátor lásky

#### Neaktivní ekonomické příkazy:

- /e buy - Nákup předmětů

- /e job - Práce a zaměstnání

- /e shop - Obchod s předměty

## 🔧 Funkce rolí

#### Bot automaticky oznamuje přiřazení rolí v nastaveném kanálu. Když uživatel obdrží roli, objeví se embed zpráva s informacemi:

    👤 Uživatel, který roli obdržel

    🎭 Přidělená role

    👮 Uživatel, který roli přiřadil

    ⏰ Čas přiřazení

## 🐛 Řešení problémů
#### Časté problémy:

    Bot nereaguje na příkazy

    Zkontrolujte token v Main.java

    Ověřte oprávnění bota na serveru

    Oznámení o rolích se nezobrazují

    Zkontrolujte LOG_CHANNEL_ID v RoleUpdate.java
    Ověřte, zda má bot oprávnění k zápisu do kanálu

    Ekonomické příkazy nefungují

    Některé ekonomické funkce jsou ve vývoji

    Používejte pouze funkční příkazy uvedené výše

## 📝 Vývoj

#### Chcete-li přidat nové příkazy:

    Vytvořte novou třídu v balíčku commands

    Implementujte Command rozhraní

    Přidejte registraci v Listener.java

## 🤝 Přispívání

#### Přispěvatelé jsou vítáni!

    Forkněte repozitář

    Vytvořte feature branch

    Commitněte změny

    Pushněte do branch

    Otevřete Pull Request


## support: [Discord server](https://discord.gg/d5V3vmMByb)


