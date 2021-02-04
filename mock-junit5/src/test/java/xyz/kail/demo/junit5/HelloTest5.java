package xyz.kail.demo.junit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runners.Parameterized;


@DisplayName("jupiter 入门")
public class HelloTest5 {

    @BeforeEach
    public void beforeTest() {
        System.out.println("@BeforeEach");
    }

    @Test
    @TestFactory
    @DisplayName("测试1")
    public void testOne() {
        System.out.println("测试1");
    }

}
