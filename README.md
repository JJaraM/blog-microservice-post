# Post Web Service
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/eab4ac3cb26843d0b860de6c8d344ffa)](https://app.codacy.com/manual/JJaraM/blog-microservice-post?utm_source=github.com&utm_medium=referral&utm_content=JJaraM/blog-microservice-post&utm_campaign=Badge_Grade_Dashboard)
[![codecov](https://codecov.io/gh/JJaraM/blog-microservice-post/branch/master/graph/badge.svg)](https://codecov.io/gh/JJaraM/blog-microservice-post)
[![Build Status](https://travis-ci.org/JJaraM/blog-microservice-post.svg?branch=master)](https://travis-ci.org/JJaraM/blog-microservice-post)
[![Maintainability](https://api.codeclimate.com/v1/badges/831a3fc398e9b20dd58c/maintainability)](https://codeclimate.com/github/JJaraM/blog-microservice-post/maintainability)
[![HitCount](http://hits.dwyl.com/JJaraM/blog-microservice-post.svg)](http://hits.dwyl.com/JJaraM/blog-microservice-post)
[![Heroku](https://heroku-badge.herokuapp.com/?app=blog-microservice-post&style=flat)](https://blog-microservice-post.herokuapp.com/)

# Deploy 
[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

# Configuration
To run the application you will need to configure the following environment properties:

* MONGODB_URI
* REDIS_URI
* REDIS_CHANNEL_TAG
* MONGODB_NAME
* AUTHORIZATION_SERVER

# How to run in Postman?
To run an example of the api you need to enter to postman and run the following operation as ``GET``

``
https://blog-microservice-api-gateway.herokuapp.com/post/0/10/0
``

Then go to ``Authorization`` tab and select ``OAuth2.0`` as the type, next to there is a button called ``Get New Access Token`` enter the following data.

```
Token Name: Blog Token name (optional)
Gran Type: Password Credentials
Access Token URL: https://blog-microservice-oauth-server.herokuapp.com/auth/realms/blog/protocol/openid-connect/token
Username: mike@other.com
Password: pass
Client ID: newClient
Client Secret: newClientSecret
Scope: read
Client Authentication: Send as Basic Auth header
```

# How to run in Docker?
To run in docker you need to run the following commands:
* mvn clean package
* sudo docker run -d -p 8080:8080 -v properties.yml spring-boot:1.0

The properies.yml will contains the environment variables like:

```
MONGODB_URI:
REDIS_CHANNEL_TAG:
REDIS_URL:
MONGODB_NAME: 
AUTHORIZATION_SERVER:
```

# About 
This is a web service that is used to feed my personal web site, and it's showing the post information.

# Technology Stack
* Spring Web Flux
* Eureka Service Discovery
* Reactive API
* MongoDB

