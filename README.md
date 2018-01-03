MyRetail-Products-v1
======================================

Summary
-------
The project is a Maven archetype for Spring Boot rest api. The project provides a composite rest service,
providing a products API, which will aggregate product data from multiple sources and return it as JSON to the
caller.

Generated project characteristics
-------------------------
* No-xml Spring REST API
* Spring Rest Template
* Spring Data
* Spock/Groovy
* MongoDB (Embedded Mongo DB)

Prerequisites
-------------

- JDK 8
- Maven 3


Run the Project
-----------

Navigate to the project directory and issue maven run command: 
to build issue the maven command: ``mvn clean install`` 
to run the project issue the maven command:``mvn spring-boot:run``


Test in the browser
---------------

Project API usage and documentation avaiable @ http://localhost:11080/swagger-ui.html

Sample requests
---------------

``Save product price``

request type: POST

url: http://localhost:11080/myretail/product/price

payload:
{
"product_id" : "13860428",
"value":"15.49",
"currency_code":"USD"
}

``Update product price``

request type: PUT

url: http://localhost:11080/myretail/product/13860428

payload:
{
"product_id" : "13860428",
"value":"15.49",
"currency_code":"USD"
}

``Fetch product details``


request type: GET

url: http://localhost:11080/myretail/product/13860428
