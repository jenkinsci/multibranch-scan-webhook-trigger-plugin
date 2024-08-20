# Multibranch Scan Webhook Trigger

[![Build Status](https://ci.jenkins.io/job/Plugins/job/multibranch-scan-webhook-trigger-plugin/job/master/badge/icon)](https://ci.jenkins.io/job/Plugins/job/multibranch-scan-webhook-trigger-plugin)

All multibranch projects come with a built-in periodical scan trigger that polls scm and checks which branches have changed and then builds those branches.

This is a Jenkins plugin that adds functionality to do this scan on a webhook:

1.  Receive any HTTP request, `JENKINS_URL/multibranch-webhook-trigger/invoke?token=TOKENHERE`
2.  Trigger a multibranch job scan that matches the token

### Configure the Token parameter

There is a special `token` parameter. When supplied, the invocation will only trigger jobs with that exact token. The token also allows invocations without any other authentication credentials.

![Parameter](https://github.com/jenkinsci/multibranch-scan-webhook-trigger/blob/master/images/configure-token.png)

The token can be supplied as a:

- Request parameter: `curl -POST http://localhost:8080/jenkins/multibranch-webhook-trigger/invoke?token=my-token`
- Token header: `curl -POST -H "token: my-token" http://localhost:8080/jenkins/multibranch-webhook-trigger/invoke`
