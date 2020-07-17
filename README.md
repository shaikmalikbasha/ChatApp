# ChatApp

## Requirements:
- Java 8 or above version should be installed. If you want to install now then please visit this [link](https://java.com/en/download/help/download_options.xml) and go through the instructions.
- Maven should be installed. If you want install now then please visit this [link](https://maven.apache.org/install.html) and go through the instructions.
- MySQL Database should be installed. If you want to install now then please visit this [link]() and go through the instructions.

## Set up
1. git clone https://github.com/shaikmalikbasha/ChatApp.git
2. cd ChatApp
3. vi src/main/resources/application.properties [Update your database name and mysql credentials]
4. vi src/main/resources/application-dev.properties. [Change the directory for uploads]
5. mvn clean package
6. mvn spring-boot:run
