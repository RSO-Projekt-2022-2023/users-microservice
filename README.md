# RSO: Image metadata microservice

## Prerequisites

```bash
docker run -d --name user-db -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=users -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar user-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/users

## Run in IntelliJ IDEA
Add new Run configuration and select the Application type. In the next step, select the module api and for the main class com.kumuluz.ee.EeApplication.

Available at: localhost:8080/v1/users

## Docker commands
```bash
docker build -t user-api-image .   
docker images
docker run user-api-image    
docker tag user-api-image burton588/user-api-image   
docker push burton588/user-api-image
docker ps
```



