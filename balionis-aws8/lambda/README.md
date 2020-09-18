# Lambda

## Package

```
$ ./gradlew clean build
``` 

## Deploy (dynamo)

```
$ aws dynamodb create-table --table-name Rides \
	--attribute-definitions "AttributeName=RideId,AttributeType=S" \
	--key-schema "AttributeName=RideId,KeyType=HASH" \
	--provisioned-throughput "ReadCapacityUnits=5,WriteCapacityUnits=5"
{
    "TableDescription": {
	...
        "TableArn": "arn:aws:dynamodb:eu-west-2:613877803204:table/Rides",
        "TableId": "4e3c6eeb-eae3-47d6-8dba-cc0d835022b0"
    }
}
```

## Deploy (lambda)

```
$ aws iam create-role --role-name my-aws8-lambda-role --assume-role-policy-document file://config/lambda-policy.json 
{
    "Role": {
        "Path": "/",
        "RoleName": "my-aws8-lambda-role",
        "RoleId": "AROAY53PX5DCA45TDNVVR",
        "Arn": "arn:aws:iam::613877803204:role/my-aws8-lambda-role",
        "CreateDate": "2020-09-18T06:31:05+00:00",
        "AssumeRolePolicyDocument": {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Effect": "Allow",
                    "Principal": {
                        "Service": "lambda.amazonaws.com"
                    },
                    "Action": "sts:AssumeRole"
                }
            ]
        }
    }
}

$ aws iam get-role --role-name my-aws8-lambda-role

$ aws iam attach-role-policy --role-name my-aws8-lambda-role --policy-arn "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"

$ aws iam put-role-policy --role-name my-aws8-lambda-role --policy-name my-aws8-role-policy --policy-document file://config/dynamo-policy.json

$ aws iam get-role-policy --role-name my-aws8-lambda-role --policy-name my-aws8-role-policy

$ aws lambda create-function --function-name my-aws8-function --runtime nodejs12.x --handler index.handler \
  --role arn:aws:iam::613877803204:role/my-aws8-lambda-role \
  --zip-file fileb://build/balionis-aws8-lambda-1.0-SNAPSHOT.zip
{
    "FunctionName": "my-aws8-function",
    "FunctionArn": "arn:aws:lambda:eu-west-2:613877803204:function:my-aws8-function",
    "Runtime": "nodejs12.x",
    "Role": "arn:aws:iam::613877803204:role/my-aws8-lambda-role",
    "Handler": "index.handler",
...
    "LastUpdateStatus": "Successful"
}

$ aws lambda invoke --function-name my-aws8-function --cli-binary-format raw-in-base64-out \
      --payload file://src/test/data/request.json build/response.json ; cat build/response.json
{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
{"statusCode":201,"body":"{\"RideId\":\"7s58OHy_Qg1G_UF7hRQavQ\",\"Unicorn\":{\"Name\":\"Rocinante\",\"Color\":\"Yellow\",\"Gender\":\"Female\"},\"UnicornName\":\"Rocinante\",\"Eta\":\"30 seconds\",\"Rider\":\"the_username\"}","headers":{"Access-Control-Allow-Origin":"*"}}

```

## Deploy (gateway)

```
$ aws apigateway create-rest-api --name 'my-aws8-api' --region eu-west-2
{
    "id": "5scvbqk2bc",
    "name": "my-aws8-api",
    "createdDate": "2020-09-18T09:53:39+02:00",
    "apiKeySource": "HEADER",
    "endpointConfiguration": {
        "types": [
            "EDGE"
        ]
    }
}

$ aws apigateway create-authorizer --rest-api-id '5scvbqk2bc' --name 'my-aws8-api-authorizer' \
    --type COGNITO_USER_POOLS --provider-arns 'arn:aws:cognito-idp:eu-west-2:613877803204:userpool/eu-west-2_Vi7WVYVkn' \
    --identity-source 'method.request.header.Authorization'
{
    "id": "tdc92u",
    "name": "my-aws8-api-authorizer",
    "type": "COGNITO_USER_POOLS",
    "providerARNs": [
        "arn:aws:cognito-idp:eu-west-2:613877803204:userpool/eu-west-2_Vi7WVYVkn"
    ],
    "authType": "cognito_user_pools",
    "identitySource": "method.request.header.Authorization"
}


$ aws apigateway get-resources --rest-api-id 5scvbqk2bc --region eu-west-2
{
    "items": [
        {
            "id": "d25hga0x60",
            "path": "/"
        }
    ]
}

$ aws apigateway create-resource --rest-api-id 5scvbqk2bc --region eu-west-2 \
      --parent-id d25hga0x60 \
      --path-part ride
{
    "id": "4r8vvt",
    "parentId": "d25hga0x60",
    "pathPart": "ride",
    "path": "/ride"
}

$ aws apigateway put-method --rest-api-id 5scvbqk2bc --region eu-west-2 \
         --resource-id 4r8vvt \
         --http-method POST \
         --authorization-type "COGNITO_USER_POOLS" \
         --authorizer-id "tdc92u"
{
    "httpMethod": "POST",
    "authorizationType": "COGNITO_USER_POOLS",
    "authorizerId": "tdc92u",
    "apiKeyRequired": false
}

$ aws apigateway put-integration --rest-api-id 5scvbqk2bc --region eu-west-2 \
        --resource-id 4r8vvt \
        --http-method POST \
        --integration-http-method POST \
        --type AWS_PROXY \
        --uri "arn:aws:apigateway:eu-west-2:lambda:path/2015-03-31/functions/arn:aws:lambda:eu-west-2:613877803204:function:my-aws8-function/invocations"
{
    "type": "AWS_PROXY",
    "httpMethod": "POST",
    "uri": "arn:aws:apigateway:eu-west-2:lambda:path/2015-03-31/functions/arn:aws:lambda:eu-west-2:613877803204:function:my-aws8-function/invocations",
    "passthroughBehavior": "WHEN_NO_MATCH",
    "timeoutInMillis": 29000,
    "cacheNamespace": "4r8vvt",
    "cacheKeyParameters": []
}

$ aws apigateway put-method-response --rest-api-id 5scvbqk2bc --region eu-west-2 \
       --resource-id 4r8vvt --http-method POST \
       --status-code 200 --response-model '{"application/json": "Empty"}'
{
    "statusCode": "200",
    "responseModels": {
        "application/json": "Empty"
    }
}

$ aws apigateway put-integration-response --rest-api-id 5scvbqk2bc \
        --resource-id 4r8vvt --http-method POST \
        --status-code 200 --response-templates '{"application/json": "" }'

$ aws apigateway create-deployment --rest-api-id 5scvbqk2bc --stage-name prod
{
    "id": "xyegd0",
    "createdDate": "2020-09-18T09:58:51+02:00"
}

// https://{restapi_id}.execute-api.{region}.amazonaws.com/{stage_name}/{method_name}
$ curl -s -X POST \
    -H "Accept: *.*" -H "Content-Type: application/json" \
    -H "Authorization: eyJraWQiOiJ0WGNkNUF0aDNkdkxtaFlMclRDWkI2S0R4d3pNYWN2Tko4YXV5Nkx1a3VJPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJkY2IyODJhZi02YWE2LTRkZTYtYTEzYy1mOGViN2U1NzFjNzIiLCJhdWQiOiIyaWxoZ2RmMWE4bThuMHVzNTJsZWRpM2JoMyIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJldmVudF9pZCI6ImJjZTU2ZWJhLTFhOGMtNDZlZi04NjI1LTBiYzk1ZjIwODQwNCIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjAwNDE2MjUwLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtd2VzdC0yLmFtYXpvbmF3cy5jb21cL2V1LXdlc3QtMl9WaTdXVllWa24iLCJjb2duaXRvOnVzZXJuYW1lIjoiYXJ2eWRhcy1hdC1iYWxpb25pcy5jb20iLCJleHAiOjE2MDA0MTk4NTAsImlhdCI6MTYwMDQxNjI1MCwiZW1haWwiOiJhcnZ5ZGFzQGJhbGlvbmlzLmNvbSJ9.puxNHZUwu0DeSWv4PGWlZ-4g4TSwv0P3penQt7i6-PBsVwaJtljayGatEVrufygeGUpFykwxJ5KrFcFNrrLwHIKsBtJm_a_cmo2021w-mBPVhLucv9Q5iYkceBxC9Op11aJoQpPZE5PVLU8rLBhc1xjOt-SjCNHyTBIAUyu3nXcfjQ8sIYElYPx59I54DNKO0JcAxjo7fdJ9sFwfg_5GifKFFGdDCJIWub5_ghXSz4kMjCpHhqf9rpkcIgCOZxvo6Zg9JVyHrCodNsdC6waBD4GTuae9OYUUg1b-XYVo1JuYyUXEa99xfHOkk-DNfY6mtbI3uxbZd11mbaH8dOFJxA" \
    -d '{"PickupLocation":{"Latitude":47.6083913234893,"Longitude":-122.30742507934372}}' \
    https://5scvbqk2bc.execute-api.eu-west-2.amazonaws.com/prod/ride
{"RideId":"xQ5TtD4bIABF4bB6lY_aUg","Unicorn":{"Name":"Shadowfax","Color":"White","Gender":"Male"},"UnicornName":"Shadowfax","Eta":"30 seconds","Rider":"arvydas-at-balionis.com"}
```