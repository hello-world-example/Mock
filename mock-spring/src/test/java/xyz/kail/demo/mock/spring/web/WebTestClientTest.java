package xyz.kail.demo.mock.spring.web;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;
import xyz.kail.demo.mock.controller.HelloController;

import javax.annotation.Resource;

public class WebTestClientTest {

    @Resource
    private WebApplicationContext wac;

    @Test
    public void bindToController() {
        final WebTestClient testClient = WebTestClient.bindToController(new HelloController()).build();
        testClient.get()
                .uri("/demo/hello/say/{world}?p1=P", "W")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.p1").value(Matchers.equalTo("P"));
    }

}
