# test-api
Basic API test framework

Prerequisites:
---------------
* Java 21 (e.g. [jdk-21.0.6+7 AdoptOpenJDK](https://adoptopenjdk.net/))

Instructions:
---------------
* `./mvnw clean test` - run tests
* Test results are stored in `target/test-report` folder in .html format
* `./mvnw allure:serve` - optionally serve Allure report
* Logs are output to console with INFO level, can be configured in `src/test/resources/log4j2.xml`