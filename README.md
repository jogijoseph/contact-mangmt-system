# contact-mangmt-system

## Overview

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
