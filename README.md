# Home Assignment

## Get feature by User
```shell
  curl -v "http://localhost:9191/demo/feature?email=tyeesheng@yahoo.com&featureName=loan"
```

## Update feature by User
```shell
    curl --header "Content-Type: application/json" \
    -d "{\"email\":\"tyeesheng@yahoo.com\", \"featureName\":\"loan\", \"enable\":\"false\"}" \
    -v http://localhost:9191/demo/feature
```