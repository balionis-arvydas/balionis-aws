# balionis-aws8

This sandbox is to remind me how to do 1) web (with amplify + s3) , 2) cognito (for authentication) and 3) kotlin lambda (for backend).

The source for above is https://aws.amazon.com/getting-started/hands-on/build-serverless-web-app-lambda-apigateway-s3-dynamodb-cognito/ 
except setup is done via "aws cli" rather "aws console".

## Web (Amplify)

See [./web/README.md](./web/README.md)

## Security (Cognito)

```
$ aws cognito-idp create-user-pool --pool-name balionis-aws8-users
{
    "UserPool": {
        "Id": "eu-west-2_fxjx8gPKy",
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
// ...
}

$ aws cognito-idp create-user-pool-client --user-pool-id eu-west-2_fxjx8gPKy \
    --client-name balionis-aws8-users-client --no-generate-secret
{
    "UserPoolClient": {
        "UserPoolId": "eu-west-2_fxjx8gPKy",
        "ClientName": "balionis-aws8-users-client",
        "ClientId": "6ijp32grl9fosof9p18mlg18tj",
        "LastModifiedDate": "2020-09-16T19:06:20.705000+02:00",
        "CreationDate": "2020-09-16T19:06:20.705000+02:00",
        "RefreshTokenValidity": 30,
        "AllowedOAuthFlowsUserPoolClient": false
    }
}
```
