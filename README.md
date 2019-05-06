# Multibranch Scan Webhook Trigger

[![Build Status](https://ci.jenkins.io/job/Plugins/job/multibranch-scan-webhook-trigger-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/multibranch-scan-webhook-trigger-plugin)

All multibranch projects comes with build in periodically scan trigger that polls scm and check wich branches has changed and than build those branches.

This is a Jenkins plugin that add functionality to do thi scan on webhook:

 1. Receive any HTTP request, `JENKINS_URL/multibranch-webhook-trigger/invoke?token=TOKENHERE`
 2. Trigger a multibranch jobs scan that matches the token



### Configure the Token parameter

There is a special `token` parameter. When supplied, the invocation will only trigger jobs with that exact token. The token also allows invocations without any other authentication credentials.

![Parameter](https://github.com/jenkinsci/multibranch-scan-webhook-trigger/blob/master/images/configure-token.png)

The token can be supplied as a:

* Request parameter: `curl -vs http://localhost:8080/jenkins/generic-webhook-trigger/invoke?token=abc123 2>&1`
* Token header: `curl -vs -H "token: abc123" http://localhost:8080/jenkins/generic-webhook-trigger/invoke 2>&1`

