![Maven Build](https://github.com/raonigabriel/spring-qrcode-example/workflows/Maven%20Build/badge.svg?branch=1.5.7)
[![Codecov](https://codecov.io/gh/raonigabriel/spring-qrcode-example/branch/master/graph/badge.svg)](https://codecov.io/gh/raonigabriel/spring-qrcode-example)
[![Libraries](https://img.shields.io/librariesio/github/raonigabriel/spring-qrcode-example)](https://libraries.io/github/raonigabriel/spring-qrcode-example)

Spring Boot QrCode Example
-------------------
Demonstrates some of the capabilities of the Spring Boot framework through a small, simple example.
After reviewing this example, you should have a good understanding of what Spring Boot can do and get a feel for how easy it is to use.
#Features:

1. [Spring Boot 2.5.x](https://github.com/raonigabriel/spring-qrcode-example/blob/master/pom.xml#L39), supporting fully executable JARs for Linux based operating systems, including [“service” support](https://github.com/raonigabriel/spring-qrcode-example/blob/master/pom.xml#L100)
2. WebFlux rest controller to [generate qrcode images](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L54)
  1. Produces binary [Content-Type (PNG)](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L53)
  2. HTTP header manipulation [(Cache-Control)](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L56)
  3. Java [Exception translation](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L73) to HTTP status code
  4. Manual cache eviction [(HTTP DELETE)](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L62)
  5. CORS enabled [(GET, DELETE)](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L40)
3. [Reactive processing](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/ImageService.java#L35) (for the image creation)
  1. Uses the [Google zxing library](https://github.com/raonigabriel/spring-qrcode-example/blob/master/pom.xml#L78)
4. Spring IoC ([Service](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/ImageService.java#L29), [Autowired](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L47))
5. [Backend caching](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/ImageService.java#L30) (Spring "simple" memory-based ConcurrentHashMap)
6. Schedulled tasks. Automatic cache eviction, [every 30 minutes](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/SpringExampleApp.java#L60).
7. [Logging](https://github.com/raonigabriel/spring-qrcode-example/blob/master/src/main/java/com/github/raonigabriel/qrcode/ImageService.java#L33) (sl4j + logback)
8. Only 2 classes, about 100 lines of code!!! 
9. Small. Final JAR includes everything (it self-contained) and it's about 17 MB


To get the code:
-------------------
Clone the repository:

    $ git clone https://github.com/raonigabriel/spring-qrcode-example.git

If this is your first time using Github, review http://help.github.com to learn the basics.

To run the application:
-------------------	
From the command line with Maven:

    $ cd spring-qrcode-example
    $ mvn spring-boot:run 

From the command line with Linux:

    $ cd spring-qrcode-example/target
    $ ./spring-qrcode-example-x.y.z.jar

Using docker:

    $ docker run -d --name qrcode-service --rm -p 8080:8080 raonigabriel/spring-qrcode-example


Access the deployed web application [http://localhost:8080/qrcode?text=Hello%20World%20From%20Spring](http://localhost:8080/qrcode?text=Hello%20World%20From%20Spring)

or

In your preferred IDE such as SpringSource Tool Suite (STS) or IDEA:

* Import spring-qrcode-example as a Maven Project

## License

Released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html)

## Disclaimer
* This code comes with no warranty. Use it at your own risk.
* I don't like Apple. Fuck off, fan-boys.
* I don't like left-winged snowflakes. Fuck off, code-covenant. 
* I will call my branches the old way. Long live **master**, fuck-off renaming.
