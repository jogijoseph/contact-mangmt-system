# contact-mangmt-system

## Overview
The Contact Management System is used to record Users contact details. This application provides following functionalities:
1. Able to add users with following data
      a. Name
      b. Address
      c. Country Code
      d. Phone Number
      c. Email
2. Delete a specific User.
3. Able to fetch the details of a specific User.
4. Able to fetch details of all the Users.   

## Database Setup
This setup is following the postgres setup using [docker](https://www.docker.com/).

1. Run the command `docker pull postgres`
2. Start the docker container for postgres

```
docker run --name cms-postgres -p49155:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```
3. Verify that the DB server is running using and Postgres client like [pgAdmin](https://www.pgadmin.org/).
4. Create the database `cms-postgresql` using client (only for the first time).

## Runtime variables

server.port=9090

## Technology Stack
- Postgres 11
- Spring Boot 2.6
- Java 11

## Running the application
1. Run the following command to create an executable jar file.
```
mvn clean package
```
1. Run the below command to start up the Spring Boot application.
```
java -jar target/cms-0.0.1-SNAPSHOT.jar
```

## Endpoints
| URL                                       | HTTP Request Metods | Description                                 |
| ------------------------------------------|:-------------:      | :-------------------------------------------|
| http://localhost:9090/cms/addUser         |       post          | To add single User details.                 |
| http://localhost:9090/cms/addUsers        |       post          | To add multiple User details.               |
| http://localhost:9090/cms/user/<id>       |       get           | To get the details of a User.               |
| http://localhost:9090/cms/users           |       get           | To get details of all Users.                |
| http://localhost:9090/cms/user/delete/<id>|       delete        | To delete a User.                           |
| http://localhost:9090/cms/upload          |       post          | To add details of Users throug file upload. |
