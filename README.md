# IBM cloud S3 bucket document authentication microservice

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

This project runs on Spring ```2.2.4```, java ```1.8``` and ibm cos sdk ```2.6.1```

### Installing

Clone the repo and then run:

```mvn clean install```

to create the jar. To run the program run:

``` cd target```

```java -jar rg_auth_svc-0.0.1.jar```

This will start the program on port 8080. 

### REST API
 The rest api to get a document is described below.
 
#### Get Document

#####Request
  
 ```GET /document?uid=<uid>&file=<file name>```
 
 #####Response
 
```https://<example pre-signed url>```

## Deployment

How to deploy this app

### Build a docker image and run locally

```docker build . -t rg_auth_svc```

``` docker run -it -p 8080:8080 rg_auth_svc:latest```

Service will be running on ```http://localhost:8080/```

### Deploy to openshift

Build docker image.
``` docker build . -t uk.icr.io/ix-liberty/auth-service```

Push image to container registry.

```docker push```

Login to the cluster to deploy to and then run:

```oc create -f boilerplate.yaml```