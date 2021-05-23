# mock-sync-service

## Build

```shell
cd ..
./gradlew clean mock-sync-service:dockerComposeUp
```

## Test (local)

```shell
curl -X GET http://localhost:8080/api/scenario/s0 
```
