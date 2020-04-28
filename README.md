# Post Web Service
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
```

# About 
This is a web service that is used to feed my personal web site, and it's showing the post information.

# Technology Stack
* Spring Web Flux
* Eureka Service Discovery
* Reactive API
* MongoDB

# Verify
