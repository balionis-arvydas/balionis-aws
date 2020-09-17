# Web

## Package

```
$ ./gradlew clean build

$ aws s3 mb s3://balionis-aws8-web

$ aws s3 cp ./build/balionis-aws8-web-1.0-SNAPSHOT.zip s3://balionis-aws8-web

$ aws s3 ls s3://balionis-aws8-web
2020-09-16 11:07:33   11464152 balionis-aws8-web-1.0-SNAPSHOT.zip

``` 

## Deploy 

```
$ aws amplify create-app --name balionis-aws8-web
{
    "app": {
        "appId": "d3qi80ycr1p7c0",
        "appArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d3qi80ycr1p7c0",
        "name": "balionis-aws8-web",
        "platform": "WEB",
        "createTime": "2020-09-16T12:25:21.541000+02:00",
        "updateTime": "2020-09-16T12:25:21.541000+02:00",
        "defaultDomain": "d3qi80ycr1p7c0.amplifyapp.com",
        "enableBranchAutoBuild": false,
        "enableBranchAutoDeletion": false,
        "enableBasicAuth": false,
        "customRules": [],
        "enableAutoBranchCreation": false
    }
}

$ aws amplify create-branch --app-id d3qi80ycr1p7c0 --branch-name dev
{
    "branch": {
        "branchArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d3qi80ycr1p7c0/branches/dev",
        "branchName": "dev",
        "stage": "NONE",
        "displayName": "dev",
        "enableNotification": false,
        "createTime": "2020-09-16T12:35:39.390000+02:00",
        "updateTime": "2020-09-16T12:35:39.390000+02:00",
        "enableAutoBuild": true,
        "totalNumberOfJobs": "0",
        "enableBasicAuth": false,
        "thumbnailUrl": "https://aws-amplify-prod-eu-west..."
		///...
	}
}

$ aws amplify start-deployment --app-id d3qi80ycr1p7c0 --branch-name dev \
	--source-url s3://balionis-aws8-web/balionis-aws8-web-1.0-SNAPSHOT.zip
{
    "jobSummary": {
        "jobArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d3qi80ycr1p7c0/branches/dev/jobs/0000000001",
        "jobId": "1",
        "status": "PENDING"
    }
}

$ aws amplify get-job --app-id d3qi80ycr1p7c0 --branch-name dev --job-id 3 | grep status

// curl -s -X GET https://{branch-name}.{app-id}.amplifyapp.com
$ curl -s -X GET https://dev.d3qi80ycr1p7c0.amplifyapp.com | head -20

$ aws amplify create-domain-association --app-id d3qi80ycr1p7c0 --domain-name tenu.com --sub-domain-settings "prefix=dev,branchName=dev"
{
    "domainAssociation": {
        "domainAssociationArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d3qi80ycr1p7c0/domains/tenu.com",
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

$ aws amplify get-domain-association  --app-id d3qi80ycr1p7c0 --domain-name tenu.com
{
    "domainAssociation": {
        "domainAssociationArn": "arn:aws:amplify:eu-west-2:613877803204:apps/d3qi80ycr1p7c0/domains/tenu.com",
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
