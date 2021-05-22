# balionis-aws8

This sandbox is to remind me how to do 1) web (with amplify + s3) , 2) cognito (for authentication) and 3) kotlin lambda (for backend).

The source for above is https://aws.amazon.com/getting-started/hands-on/build-serverless-web-app-lambda-apigateway-s3-dynamodb-cognito/ 
except setup is done via "aws cli" rather "aws console".

## Web (Amplify)

See [./web/README.md](./web/README.md)

## Security (Cognito)

```
$ aws cognito-idp create-user-pool --pool-name balionis-aws8-users --auto-verified-attributes email
{
    "UserPool": {
        "Id": "eu-west-2_Vi7WVYVkn",
        "Name": "balionis-aws8-users",
        "Policies": {
            "PasswordPolicy": {
                "MinimumLength": 8,
                "RequireUppercase": true,
                "RequireLowercase": true,
                "RequireNumbers": true,
                "RequireSymbols": true,
                "TemporaryPasswordValidityDays": 7
            }
        },
        "LambdaConfig": {},
// ...
        "Arn": "arn:aws:cognito-idp:eu-west-2:613877803204:userpool/eu-west-2_Vi7WVYVkn"
    }
}

$ aws cognito-idp create-user-pool-client --user-pool-id eu-west-2_Vi7WVYVkn \
    --client-name balionis-aws8-users-client --no-generate-secret
{
    "UserPoolClient": {
        "UserPoolId": "eu-west-2_Vi7WVYVkn",
        "ClientName": "balionis-aws8-users-client",
        "ClientId": "2ilhgdf1a8m8n0us52ledi3bh3",
        "LastModifiedDate": "2020-09-18T08:12:38.660000+02:00",
        "CreationDate": "2020-09-18T08:12:38.660000+02:00",
        "RefreshTokenValidity": 30,
        "AllowedOAuthFlowsUserPoolClient": false
    }
}

$ vi balionis-aws8\web\public\js\config.js
window._config = {
    cognito: {
        userPoolId: 'eu-west-2_Vi7WVYVkn', 
        userPoolClientId: '2ilhgdf1a8m8n0us52ledi3bh3', 
        region: 'eu-west-2'
    },
	....
};
```

## Lambda (Dynamo, NodeJs)

See [./lambda/README.md](./lambda/README.md)

## Terminate resources

```
$ aws amplify delete-app --app-id d2jvwbzkj7re2a
{
     "app": {
         "appId": "d2jvwbzkj7re2a",
         "appArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d2jvwbzkj7re2a",
         "name": "balionis-aws8-web",
...
}

$ aws cognito-idp delete-user-pool --user-pool-id "eu-west-2_Vi7WVYVkn"

$ aws lambda delete-function --function-name my-aws8-function

$ aws iam detach-role-policy --role-name my-aws8-lambda-role --policy-arn "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"

$ aws iam delete-role-policy --role-name my-aws8-lambda-role --policy-name my-aws8-role-policy

$ aws iam delete-role --role-name my-aws8-lambda-role

$ aws dynamodb delete-table --table-name Rides
{
    "TableDescription": {
        "TableName": "Rides",
        "TableStatus": "DELETING",
...
}

$ aws logs describe-log-groups
{
    "logGroups": [
        {
            "logGroupName": "/aws/lambda/my-aws8-function",
            "creationTime": 1600411043198,
            "metricFilterCount": 0,
            "arn": "arn:aws:logs:eu-west-2:613877803204:log-group:/aws/lambda/my-aws8-function:*",
            "storedBytes": 0
        }
    ]
}

$ aws logs delete-log-group --log-group-name ??? 
// FIXME: "/aws/lambda/my-aws8-function" fails with: Member must satisfy regular expression pattern: [\.\-_/#A-Za-z0-9]+

$ aws apigateway delete-rest-api --rest-api-id '5scvbqk2bc'

$ aws s3 rb s3://balionis-aws8 --force
delete: s3://balionis-aws8/balionis-aws8-web-1.0-SNAPSHOT.zip
remove_bucket: balionis-aws8

```


