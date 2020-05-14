# Sjakk-Arena-backend

Sjakkarena is an app for organizing chess tournaments.


## Setup

### Requirements: 

1. [JDK 1.8](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
2. [MAVEN 3](https://maven.apache.org/download.cgi)
3. [MySQL database](https://dev.mysql.com/downloads/)

### Database

Set up the database by running the
1_sjakkarena.sql script. 

Some dummy data can be found
in the 2_sjakkarena.data.sql file.

The sql-files are found in the [SQL-script folder](SQL-script).

### Configuration

Add username and password of the database role in the
[application.properties](src/main/resources/application.properties) file found in the resources folder.


Add email-account credentials to the [EmailSender](src/main/java/no/ntnu/sjakkarena/EmailSender.java)
file.

Add public key and private key to the [KeyHelper](src/main/java/no/ntnu/sjakkarena/utils/KeyHelper) file to use 
push notifications. Public and private key kan be generated [here](https://web-push-codelab.glitch.me/)

## Running the application

Run the application by executing the following command:
```bash
mvn spring-boot:run
```
 



 
