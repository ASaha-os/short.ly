# URL Shortener Mini

A console-based, beginner-friendly **Java** application demonstrating core backend development concepts, including JDBC, H2 SQL database integration, and basic system design principles.This project was developed using a **10-step roadmap**, with each step designed as a separate Pull Request (PR) for Hacktoberfest contributions.

## 📌 Preview

- [Features](#features)
- [Prerequisites](#prerequisites)
- [How to build and run](#how-to-build-and-run)
- [Running Test](#running-tests)
- [Project Structure](#project-structure)
  
## ⭐️ Features

#### 1️⃣ URL Shortening

Generates a unique, short key (6 characters, alphanumeric) for any long URL.

**Example:**

Input:

```
Enter 1 to shorten a new URL or 2 to retrieve one:
> 1
Enter the original URL:
> https://abc.com/research/
```

Output:
```
Shortened URL created successfully!
Short Key: Xy7Ab2
```

#### 2️⃣ Database Persistence

Uses an in-memory H2 SQL database to store mappings between short keys and long URLs.


#### 3️⃣ Key Lookup 

Retrieves the original long URL given a short key.

**Example:**

Input:

```
Enter 1 to shorten a new URL or 2 to retrieve one:
> 2
Enter the short key:
> Xy7Ab2
```

Output:

```
🔗 Original URL: https://abc.com/research/
```


#### 4️⃣ Input Validation 

Ensures only correctly formatted URLs (starting with http:// or https://) are accepted.

**Example:**

Input:

```
Enter the original URL:
> abc.com
```

Output:

```
Invalid URL format! Please include http:// or https://
```

#### 5️⃣ Interactive Console 

Provides a simple command-line interface for shortening and looking up URLs.

#### 6️⃣ Unit Tested

Includes JUnit 5 tests for core components (KeyGenerator and DatabaseManager).

## ⭐️ Prerequisites

To build and run this application, you need:

- Java Development Kit (JDK) 17 or higher.

- Apache Maven (for dependency management and building the project).

- Git (for version control and contribution).

## ⭐️ How to Build and Run

1. **Clone the Repository**
   
  ```bash
  git clone [https://github.com/your-username/url-shortener-mini.git](https://github.com/your-username/url-shortener-mini.git)
  cd url-shortener-mini
  ```

2. **Build the Project**
   
  Use Maven to compile the Java files and download the necessary dependencies (like the H2 database driver).
  
  ```bash
  mvn clean install
  ```

3. **Run the Application**
   
  Execute the main class directly using the Maven Exec plugin.
  ```
  mvn exec:java
  ```

The application will start, connect to the in-memory H2 database, initialize the table schema, and display the interactive menu.

## ⭐️ Running Tests

All core functionality (key generation, saving, and lookup) is covered by JUnit 5 tests. 
You can run them with a single Maven command:

```bash
mvn test
```

## 📌 Project Structure
```plaintext
url-shortener-mini/
├── pom.xml                               # Maven configuration (Dependencies: H2, JUnit 5)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourcompany/urlshortener/
│   │   │       ├── UrlShortenerApp.java      # Main app loop and user interface (PR 7, 8)
│   │   │       ├── DatabaseManager.java      # JDBC connection, CRUD operations (PR 3, 4, 5, 6)
│   │   │       └── KeyGenerator.java         # Key generation logic (PR 2)
│   │   └── resources/
│   │       └── application.properties        # Database connection settings (PR 3)
│   └── test/
│       └── java/
│           └── com/yourcompany/urlshortener/
│               ├── KeyGeneratorTest.java     # Unit tests for key creation
│               └── DatabaseManagerTest.java  # Integration tests for database logic
└── README.md                               # Project documentation (PR 10)
```
