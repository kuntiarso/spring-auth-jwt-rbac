# Spring Security, JWT, RBAC, and Refresh Token

A demo REST API authentication and authorization services with Spring Boot and Spring Security.

## Getting Started

### Prerequisites

##### Java Development Kit (JDK) 17

As this project using Spring Boot 3, so it's required to install Java 17 as minimum compatible version.

install Java 17 [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).   
or   
install with SDKMAN:

```
sdk install java 17.0.7
sdk default java 17.0.7
java -version
```

##### Gradle

This project use gradle groovy as build tool, so it's requried to have this one installed.

download and install manually [here](https://gradle.org/releases).   
or   
install with SDKMAN:

```
sdk install gradle 8.1.1
sdk default gradle 8.1.1
gradle -v
```

##### VS Code (recommended)

Please install `eslint` and `prettier` plugins on VS Code to apply code consistency and autoformatting.

### Configuration

Fork or download the project to your machine. Then open the project's folder and in the project's root folder please create your own `.env` file. Follow the env structure from `.env.template` file or you could modify yourself.

### Migration (Flyway)

I disabled auto migration feature in this project from `application.properties` config, so when spring app is starting it wouldn't trigger flyway to migrate. I moved the auto migration responsibility to be manually handled with the help of gradle.

run migration:

```
gradle flywayMigrate
```

run other migration tasks:

```
gradle <migration task>
```

how see migration tasks:

```
gradle tasks
```

### Run project

run development:

```
gradle bootRun
```

run build:

```
gradle build
```

## Built With

- Spring Boot 3
- Spring Security
- Spring Data JPA

## Authors

- **[Kuntiarso](https://github.com/kuntiarso)** - _2023_

