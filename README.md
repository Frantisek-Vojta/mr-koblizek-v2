# mr. Koblizek v2

A modern Discord bot built in Java using the JDA (Java Discord API) library, featuring an integrated economy system and administrative utility tools.

## Prerequisites

- **Java:** Azul Zulu Community 21 or higher
- **Build Tool:** Maven (for dependency management)
- **Discord Account:** Access to the Discord Developer Portal to create and configure the bot application

## Installation and Deployment

**1. Clone the Repository**

```bash
git clone https://github.com/Frantisek-Vojta/mr-koblizek-v2.git
cd mr-koblizek-v2
```

**2. Token Configuration**

Open the file `src/main/java/org/example/Main.java` and on line 21, replace the `TOKEN` placeholder with your production Discord bot token.

**3. Logging Channel Configuration**

Open the file `src/main/java/org/example/commands/RoleUpdate.java`, locate the `LOG_CHANNEL_ID` constant and replace it with your targeted Discord channel ID:

```java
private static final long LOG_CHANNEL_ID = 123456789012345678L;
```

**4. Compilation and Execution**

Execute the following commands to compile and launch the application:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

> **CRITICAL REQUIREMENT:** The bot must be granted Administrator permissions within the target server to ensure full operational capacity.

---

## Command Registry

### Standard Commands

| Command | Description |
| --- | --- |
| `/botinfo` | Displays technical and general information about the bot instance. |
| `/donut` | Simulates acquiring a donut item. |
| `/freenitro` | Executes a joke/prank command. |
| `/help` | Returns a comprehensive list of available commands and documentation. |
| `/meme` | Fetches and displays a random meme from the configured source. |
| `/ping` | Measures and returns the current network latency of the bot. |

### Economy System Commands (`/e ...`)

| Command | Description |
| --- | --- |
| `/e balance` | Displays the current financial balance of the executing user. |
| `/e baltop` | Generates a leaderboard showing the wealthiest users on the server. |
| `/e help` | Provides documentation specific to the economy subsystem. |
| `/e profile` | Retrieves the economy-specific profile data of the user. |
| `/e slots` | Simulates a slot machine game using economy balance. |
| `/e work` | Executes a work shift to generate economy currency. |

---

## Inactive / Legacy Features

The following features are currently registered but marked as non-functional or under active maintenance:

| Command | Description |
| --- | --- |
| `guess` | Number guessing minigame |
| `love` | Compatibility calculator |
| `/e buy` | Item purchasing module |
| `/e job` | Career and employment modules |
| `/e shop` | Item shop interface |

---

## Automated Logging Functionality

The bot includes an automated event listener that monitors role updates across the guild. When a role is assigned or removed, a structured embed is dispatched to the designated logging channel containing:

- Target user identifier
- Assigned or removed role attributes
- Executor (the administrator or system process that applied the change)
- Timestamp of the transaction

---

## Troubleshooting Matrix

**Bot does not respond to commands**
Verify the integrity of the token in `Main.java`. Confirm that the application has been granted correct permissions in the Discord Developer Portal and server.

**Role updates are not logged**
Verify that `LOG_CHANNEL_ID` matches a valid, accessible text channel. Ensure the bot has explicit write permissions for that specific channel.

**Economy module malfunctions**
Check if the command is listed under the "Inactive Features" section. Features under active development may be unstable.

---

## Development and Extensibility

To implement new command modules, adhere to the established architectural pattern:

1. Create a new class within the `commands` package.
2. Implement the core `Command` interface.
3. Register the new class instance within `Listener.java`.

---

## Contribution Guidelines

Contributions are welcome via standard GitHub workflows:

1. Fork the repository.
2. Create a dedicated feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes with clear, descriptive messages.
4. Push the branch to your remote fork and open a Pull Request.

---

## Support
Official Discord Server: https://dsc.gg/xcel-resources
