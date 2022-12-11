# RSO: Image metadata microservice

## Prerequisites

```bash
docker run -d --name user-db -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=vehicles -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar user-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8080/v1/vehicles

## Run in IntelliJ IDEA
Add new Run configuration and select the Application type. In the next step, select the module api and for the main class com.kumuluz.ee.EeApplication.

Available at: localhost:8080/v1/vehicles

## Docker commands
```bash
docker build -t vehicle-api-image .   
docker images
docker run vehicle-api-image    
docker tag vehicle-api-image burton588/vehicle-api-image   
docker push burton588/vehicle-api-image
docker ps
```



