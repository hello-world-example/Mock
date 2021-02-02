# WebTestClient

- `WebTestClient` @Since 5.0
- 为 `WebFlux` 设计



## bindToController

```java
final WebTestClient testClient = WebTestClient.bindToController(new HelloController()).build();

testClient.get()
  .uri("/demo/hello/say/{world}?p1=P", "W")
  .exchange()
  .expectStatus().isOk()
  .expectBody().jsonPath("$.p1").value(Matchers.equalTo("P"));
```





## Read More

- Spring Testing [3.6. WebTestClient](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#webtestclient)