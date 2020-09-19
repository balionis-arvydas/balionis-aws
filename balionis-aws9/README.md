# balionis-aws9

This sandbox is to remind me how to do terraform with docker and aws.

The source for below is https://learn.hashicorp.com/tutorials/terraform/install-cli 

## Install terraform

```
$ yum-config-manager --add-repo https://rpm.releases.hashicorp.com/RHEL/hashicorp.repo
$ yum -y install terraform
$ terraform -help plan
```

### Test

```
$ firewall-cmd --zone=public --permanent --add-port=8000/tcp
$ firewall-cmd --reload
$ firewall-cmd --list-all

$ git clone https://github.com/balionis-arvydas/balionis-aws
$ cd ./balionis-aws/balionis-aws9
$ touch main.tf // see contents
$ terraform init
$ terraform apply
$ curl -s -X GET http://banote17.balionis.com:8000/
```

## Configure aws 

The source for below is https://learn.hashicorp.com/collections/terraform/aws-get-started
