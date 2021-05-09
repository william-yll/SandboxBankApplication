# SandboxBankApplication
The application is ran as a spring boot application. 
It exposes 2 endpoints. 1st endpoint /transfer sends money between 2 accounts. 2nd endpoint /balance returns account
 balance and transaction history. 
When the application starts up, it is preloaded with 2 accounts with account id of 1 and 2.

Main entry point for application is SandboxBankApplication.java

## Installation:
Install latest version of Java and Maven
You may need to set your JAVA_HOME and MAVEN_HOME

Open command line in the directory, and run below 

mvn clean install

## Running Program:
Open command line in the directory, and run below 

mvn spring-boot:run

### Sample requests
Get balance <br/>
curl -X GET "http://localhost:8080/balance?accountId=1" -H "accept: application/json"

Make transfer <br/>
curl -X POST "http://localhost:8080/transfer" -H "accept: application/json" -H "Content-Type: application/json" -d
 "{ \"amount\": 50.00, \"fromAccountId\": 1, \"toAccountId\": 2}"
