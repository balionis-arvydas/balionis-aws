# Web

## Package

```
$ ./gradlew clean build

$ aws s3 mb s3://balionis-aws8

$ aws s3 cp ./build/balionis-aws8-web-1.0-SNAPSHOT.zip s3://balionis-aws8

$ aws s3 ls s3://balionis-aws8
2020-09-16 11:07:33   11464152 balionis-aws8-web-1.0-SNAPSHOT.zip

``` 

## Deploy (amplify)

```
$ aws amplify create-app --name balionis-aws8-web
{
    "app": {
        "appId": "d2jvwbzkj7re2a",
        "appArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d2jvwbzkj7re2a",
        "name": "balionis-aws8-web",
        "platform": "WEB",
        "createTime": "2020-09-18T07:49:23.953000+02:00",
        "updateTime": "2020-09-18T07:49:23.953000+02:00",
        "defaultDomain": "d2jvwbzkj7re2a.amplifyapp.com",
        "enableBranchAutoBuild": false,
        "enableBranchAutoDeletion": false,
        "enableBasicAuth": false,
        "customRules": [],
        "enableAutoBranchCreation": false
    }
}

$ aws amplify create-branch --app-id d2jvwbzkj7re2a --branch-name dev
{
    "branch": {
        "branchArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d2jvwbzkj7re2a/branches/dev",
        "branchName": "dev",
        "stage": "NONE",
        "displayName": "dev",
        "enableNotification": false,
        "createTime": "2020-09-18T07:49:55.766000+02:00",
        "updateTime": "2020-09-18T07:49:55.766000+02:00",
        "enableAutoBuild": true,
        "totalNumberOfJobs": "0",
        "enableBasicAuth": false,
        "thumbnailUrl": "https://aws-amplify-prod-eu-west-2-artifacts.s3.eu-west-2.amazonaws.com/...",
        "ttl": "5",
        "enablePullRequestPreview": false
    }
}

$ aws amplify start-deployment --app-id d2jvwbzkj7re2a --branch-name dev \
	--source-url s3://balionis-aws8/balionis-aws8-web-1.0-SNAPSHOT.zip
{
    "jobSummary": {
        "jobArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d2jvwbzkj7re2a/branches/dev/jobs/0000000001",
        "jobId": "1",
        "status": "PENDING"
    }
}
```

$ aws amplify get-job --app-id d2jvwbzkj7re2a --branch-name dev --job-id 1 | grep status

## Test

__IMPORTANT! There is no aws cli to extract the app url. Instead, use the template "https://{branch-name}.{app-id}.amplifyapp.com"__

```
$ curl -s -X GET https://dev.d2jvwbzkj7re2a.amplifyapp.com | head -20
```

or, 

1) run "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" --disable-web-security --user-data-dir=c://temp/chrome-cache
2) open https://dev.d2jvwbzkj7re2a.amplifyapp.com/signup.html
3) open https://dev.d2jvwbzkj7re2a.amplifyapp.com/ride.html

## Associate (dev.tenu.com)

```
$ aws amplify create-domain-association --app-id d2jvwbzkj7re2a \
    --domain-name tenu.com --sub-domain-settings "prefix=dev,branchName=dev" \
    --enable-auto-sub-domain
{
    "domainAssociation": {
        "domainAssociationArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d2jvwbzkj7re2a/domains/tenu.com",
        "domainName": "tenu.com",
        "enableAutoSubDomain": false,
        "domainStatus": "CREATING",
        "subDomains": [
            {
                "subDomainSetting": {
                    "prefix": "dev",
                    "branchName": "dev"
                },
                "verified": false,
                "dnsRecord": "dev CNAME <pending>"
            }
        ]
    }
}

$ aws amplify get-domain-association  --app-id d2jvwbzkj7re2a --domain-name tenu.com
{
    "domainAssociation": {
        "domainAssociationArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d2jvwbzkj7re2a/domains/tenu.com",
        "domainName": "tenu.com",
        "enableAutoSubDomain": false,
        "domainStatus": "AWAITING_APP_CNAME",
        "certificateVerificationDNSRecord": "_4debd40fe4712b3287d042a9c34d1adf.tenu.com. CNAME _b9979279b09003bd58dd025e9552f4fd.zdxcnfdgtt.acm-validations.aws.",
        "subDomains": [
            {
                "subDomainSetting": {
                    "prefix": "dev",
                    "branchName": "dev"
                },
                "verified": false,
                "dnsRecord": "dev CNAME d1w4tf1s8kz5ts.cloudfront.net"
            }
        ]
    }
}

// goto fasthosts.co.uk and configure advanced DNS cname settings.

$ curl -s -X GET https://dev.tenu.com | head -20

```

## Redeploy 

1. build
2. aws s3 cp ...
3. aws amplify start-deployment ...
4. aws amplify get-job ... // must have 3x"SUCCESS"
