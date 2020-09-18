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
