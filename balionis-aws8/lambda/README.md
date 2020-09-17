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
        "TableId": "e436abb5-c60a-4f56-90e7-465397560fa4"
    }
}
```

## Deploy (lambda)

```
$ aws iam create-role --role-name my-aws8-role --assume-role-policy-document file://config/lambda-policy.json 
{
    "Role": {
        "Path": "/",
        "RoleName": "my-aws8-role",
        "RoleId": "AROAY53PX5DCHZUM3OY7Q",
        "Arn": "arn:aws:iam::613877803204:role/my-aws8-role",
        "CreateDate": "2020-09-17T10:49:55+00:00",
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

$ aws iam get-role --role-name my-aws8-role

$ aws iam attach-role-policy --role-name my-aws8-role --policy-arn "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"

$ aws iam put-role-policy --role-name my-aws8-role --policy-name my-aws8-role-policy --policy-document file://config/dynamo-policy.json

$ aws iam get-role-policy --role-name my-aws8-role --policy-name my-aws8-role-policy

$ aws lambda create-function --function-name my-aws8-function --runtime nodejs12.x --handler index.handler \
  --role arn:aws:iam::613877803204:role/my-aws8-role \
  --zip-file fileb://build/balionis-aws8-lambda-1.0-SNAPSHOT.zip

$ aws lambda invoke --function-name my-aws8-function --cli-binary-format raw-in-base64-out \
      --payload file://src/test/data/request.json build/response.json ; cat build/response.json
{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
{"statusCode":201,"body":"{\"RideId\":\"7s58OHy_Qg1G_UF7hRQavQ\",\"Unicorn\":{\"Name\":\"Rocinante\",\"Color\":\"Yellow\",\"Gender\":\"Female\"},\"UnicornName\":\"Rocinante\",\"Eta\":\"30 seconds\",\"Rider\":\"the_username\"}","headers":{"Access-Control-Allow-Origin":"*"}}

```

