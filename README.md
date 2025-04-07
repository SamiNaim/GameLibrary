# Game Library Management System Programming 3 Project Documentation

![Java](https://img.shields.io/badge/Java-21%2B-blue)  
![Maven](https://img.shields.io/badge/Apache%20Maven-3.9.6-orange)  
![SQLite](https://img.shields.io/badge/SQLite-3.43.0-green)

A Java-based application for managing personal game collections with both CLI and GUI interfaces.

---

## Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Usage](#usage)
7. [Testing](#testing)
8. [Project Structure](#project-structure)
9. [Dependencies](#dependencies)

---

## Overview
The **Game Library Management System** is a Java application that enables users to:
- Organize game collections
- Track purchases
- Browse games by genre
- Manage accounts

Built with **Java 21**, **Maven**, and **SQLite**, it offers:
- **Modular architecture** (MVC pattern)
- **Dual interface** (CLI & Swing GUI)
- **Persistent storage** via SQLite database

---

## Features
✔ User authentication (login/signup)  
✔ CRUD operations for game management  
✔ Purchase history tracking  
✔ Multi-interface support (GUI/CLI)  
✔ Genre-based game categorization  
✔ Unit testing with JUnit

---

## Prerequisites

| Requirement       | Installation Guide                                                                                    |
|-------------------|-------------------------------------------------------------------------------------------------------|
| Java JDK 21+      | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java21) or `brew install openjdk@21` |
| Apache Maven 3.9+ | [Maven Guide](https://maven.apache.org/install.html) or `brew install maven`                          |
| SQLite 3.43+      | Included via Maven dependency                                                                         |

**Verification:**
```bash
java -version && mvn -v
```

---

## Installation
1. **Clone the repository**
   ```bash
   git clone https://github.com/SamiNaim/GameLibrary.git 
   ```
2. **Build with Maven**
   ```bash
   mvn clean install
   ```
3. **Run the application**

---

## Configuration

### Database Setup
The system uses two SQLite databases:

| Database      | Path                                | Purpose          |
|---------------|-------------------------------------|------------------|
| Production DB | `src/main/resources/gameLibrary.db` | Main application |
| Test DB       | `src/test/java/gameLibrary.db`      | Unit tests       |

Configure in `core/Config.java`:
```java
// For production:
public static final String DB_URL = "jdbc:sqlite:src/main/resources/gameLibrary.db";

// For testing (JUnit):
// public static final String DB_URL = "jdbc:sqlite:src/test/java/gameLibrary.db";
```

---

## Usage

### Launch Options
Edit `frontend/Main.java` to select interface:

```java
// For GUI (default):
// new GUIStates().initialize();

// For CLI:
// TextUIContext textUI = new TextUIContext();
// while (!textUI.isTerminated()) {
//     textUI.handle();
// }
```
### Application Flow
1. Register or log in
2. Browse/Add games
3. Make purchases
4. View library

---

## Testing

**Run JUnit tests with**:

   ```bash
   mvn test
   ```
**Test Cases Include**:
1. User authentication
2. Game CRUD operations
3. Purchase validation

---

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   ├── core/          # Domain models (Game, Genre, etc.)
│   │   ├── database/      # Data persistence layer
│   │   └── frontend/      # UI components (CLI & GUI)
│   └── resources/         # Production database
│
└── test/
    └── java/              # JUnit tests + test database
```


### Key Directories:
- **`core/`**: Contains all domain models and business logic
- **`database/`**: Handles all database operations and persistence
- **`frontend/`**: User interface implementations (both CLI and GUI)
- **`test/`**: Contains all unit tests with a dedicated test database

---

## Dependencies

### Core Libraries
| Dependency               | Version   | Purpose                      |
|--------------------------|-----------|------------------------------|
| `org.xerial:sqlite-jdbc` | 3.48.0.0  | SQLite database connectivity |
| `org.junit.jupiter`      | 5.12.0-M1 | Unit testing framework       |
| `org.openjfx:javafx`     | 21        | GUI components (for JavaFX)  |

### Development Tools
| Tool     | Version | Usage                              |
|----------|---------|------------------------------------|
| Maven    | ≥3.9.6  | Build automation & dependency mgmt |
| Java SDK | ≥21     | Runtime environment                |

View complete dependencies in [`pom.xml`](./pom.xml):
# GameLibrary
