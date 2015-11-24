Spring Boot QrCode Example
-------------------
Demonstrates some of the capabilities of the Spring Boot framework through a small, simple example.
After reviewing this example, you should have a good understanding of what Spring Boot can do and get a feel for how easy it is to use.
#Features:

1. Spring Boot 1.3.0, supporting fully executable JARs for Linux based operating systems, including “service” support
2. Rest controller to generate qrcode images
  1. Produces binary Content-Type (PNG)
  2. Uses the Google zxing library
  3. HTTP header manipulation (Cache-Control)
  4. Java Exception translation to HTTP status code
3. Async processing (for the image creation)
4. Spring IoC (Service, Autowired)
5. Spring memory-based caching (ConcurrentHashMap)
6. Logging (sl4j)
7. Only 2 classes, about 100 lines of code!!! 

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

Access the deployed web application [http://localhost:8080/qrcode?text=Hello%20World%20From%20Spring](http://localhost:8080/qrcode?text=Hello%20World%20From%20Spring)

or

In your preferred IDE such as SpringSource Tool Suite (STS) or IDEA:

* Import spring-qrcode-example as a Maven Project

## License

Released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0.html)
