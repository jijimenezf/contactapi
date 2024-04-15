# springboot-contacts-api

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/springboot-sample-app/badge.svg?branch=master)](https://coveralls.io/github/codecentric/springboot-sample-app?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Backend services [Spring Boot](http://projects.spring.io/spring-boot/) for contacts app.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.fullstack.contactapi.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
or
./mvnw spring-boot:run
```

## Credits
This application is based on an idea developed at https://morioh.com/a/695a25379144/full-stack-reactjs-application-with-spring-boot-and-postgresql

## Improvements
I made this contributions to the original idea:
- Unit testing for REST Controller
- Unit testing for uploading a file using Mockmvc
- Code coverage using JaCoCo

## Test Coverage
![Alt text](https://res.cloudinary.com/dzhfwgpoy/image/upload/v1713206450/coverage_zlqlwl.png))

## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.
