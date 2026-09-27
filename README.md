# Carpet-INF-Addition

A small Fabric Carpet extension for INF Server.

Carpet-INF-Addition adds several lightweight commands and gameplay utilities to a Minecraft server running Fabric and Carpet.

## Features

### Player Size

Change the size of the player with simple commands.

| Command          | Description                     |
| ---------------- | ------------------------------- |
| `/big`           | Increase player size            |
| `/big pro`       | Further increase player size    |
| `/big pro max`   | Maximum large size              |
| `/small`         | Decrease player size            |
| `/small pro`     | Further decrease player size    |
| `/small pro max` | Minimum small size              |
| `/normal`        | Restore normal `1×` player size |

### Hat

```text
/hat
```

Use the currently held item as a hat.

### Sit

```text
/sit
```

Allows players to sit.

### Visible Spectators

Provides a Carpet rule for controlling spectator visibility.

```text
/carpet visibleSpectators true
```

## Carpet Rules

The mod currently provides the following rules:

| Rule                | Default | Description                   |
| ------------------- | ------- | ----------------------------- |
| `commandHat`        | `ops`   | Permission level for `/hat`   |
| `commandSit`        | `ops`   | Permission level for `/sit`   |
| `visibleSpectators` | `false` | Controls spectator visibility |

## Requirements

* Minecraft **1.21.11**
* Fabric Loader **0.19.2 or newer**
* Fabric API
* Carpet **1.4.193 or newer**

## Installation

1. Install Fabric Loader for Minecraft 1.21.11.
2. Install Fabric API.
3. Install Carpet.
4. Download the latest `Carpet-INF-Addition` release.
5. Put the `.jar` file into the server's `mods` directory.
6. Start the server.

## Commands

### Player Size

Normal:

```text
/normal
```

Large:

```text
/big
/big pro
/big pro max
```

Small:

```text
/small
/small pro
/small pro max
```

The `/normal` command restores the player's normal `1×` size.

### Hat

```text
/hat
```

### Sit

```text
/sit
```

## Building From Source

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/Carpet-INF-Addition.git
cd Carpet-INF-Addition
```

Build the mod:

### Windows

```bat
gradlew.bat build
```

### Linux / macOS

```bash
./gradlew build
```

The built JAR will be located in:

```text
build/libs/
```

## Credits

This project contains portions derived from **VulpeusCarpet**:

https://github.com/Vulpeus-Server/vulpeus-carpet

Copyright (C) 2024 VulpeusServer and contributors.

The corresponding source code is licensed under the **GNU Lesser General Public License v3.0**.

## License

Carpet-INF-Addition is licensed under the **GNU Lesser General Public License v3.0 (LGPL-3.0)**.

See [LICENSE](LICENSE) for the full license text.

## Author

**INF Server**

Source code:

https://github.com/sweetljk/Carpet-INF-Addition
