![Maven Build](https://github.com/raonigabriel/spring-qrcode-example/workflows/Maven%20Build/badge.svg?branch=1.3.1)
[![Codecov](https://codecov.io/gh/raonigabriel/spring-qrcode-example/branch/master/graph/badge.svg)](https://codecov.io/gh/raonigabriel/spring-qrcode-example)
[![Libraries](https://img.shields.io/librariesio/github/raonigabriel/spring-qrcode-example)](https://libraries.io/github/raonigabriel/spring-qrcode-example)

Spring Boot QrCode Example
-------------------
Demonstrates some of the capabilities of the Spring Boot framework through a small, simple example.
After reviewing this example, you should have a good understanding of what Spring Boot can do and get a feel for how easy it is to use.
#Features:

1. Spring Boot 2.4.x, supporting fully executable JARs for Linux based operating systems, including “service” support
2. WebFlux rest controller to generate qrcode images
  1. Produces binary Content-Type (PNG)
  2. HTTP header manipulation (Cache-Control)
  3. Java Exception translation to HTTP status code
  4. Manual cache eviction (HTTP DELETE)
  5. CORS enabled (GET, DELETE)
3. Reactive processing (for the image creation)
  1. Uses the Google zxing library
4. Spring IoC (Service, Autowired)
5. Backend caching (Spring "simple" memory-based ConcurrentHashMap)
6. Schedulled tasks. Automatic cache eviction, every 30 minutes.
7. Logging (sl4j + logback)
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
    $ ./spring-qrcode-example-1.4.x.jar

Using docker:

    $ docker run -d --name qrcode-service --rm -p 8080:8080 raonigabriel:spring-qrcode-example


Access the deployed web application [http://localhost:8080/qrcode?text=Hello%20World%20From%20Spring](http://localhost:8080/qrcode?text=Hello%20World%20From%20Spring)

or

In your preferred IDE such as SpringSource Tool Suite (STS) or IDEA:

* Import spring-qrcode-example as a Maven Project

## License

Released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html)
