terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 2.70"
    }
  }
}

provider "aws" {
  profile = "default"
  region  = "eu-west-2"
}

resource "aws_instance" "example" {
  ami           = "ami-09b89ad3c5769cca2"
  instance_type = "t2.micro"
//  vpc_security_group_ids = ["sg-0077..."]
//  subnet_id              = "subnet-923a..."
}

