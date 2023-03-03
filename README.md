# Technical Assignment

## Overview
A simple application that exposes two APIs to manage users' access to features. 
The application is using in-memory database to store records of feature accesses.

## Database structure
The following is the database structure of `features` table.

| Column         | Description                                       |
|----------------|---------------------------------------------------|
| `id`           | Generated sequential ID and is a primary key.     |
| `feature_name` | Feature name is a unique constraint.              |
| `email`        | User email is a unique constraint.                |
| `enable`       | Flag to determine feature is enabled or disabled. |

## Build
```shell
./gradlew clean build
```

## Run
```shell
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

## Testing
Run spring boot application first. Then, use either `Swagger UI` or the `cURL` command line with example shown below to test the APIs. 


### Swagger 2 UI
Test API on on Swagger UI
```shell
http://localhost:9191/swagger-ui/index.html
```

### cURL
#### Add/Update feature access
Add or update feature access.
```shell
    curl --header "Content-Type: application/json" \
    -d "{\"email\":\"newUser@gmail.com\", \"featureName\":\"newFeature\", \"enable\":\"true\"}" \
    -v http://localhost:9191/feature
```
#### Get feature by User
Retrieve user access by using email and feature name.
```shell
  curl -v "http://localhost:9191/feature?email=newUser@gmail.com&featureName=newFeature"
```