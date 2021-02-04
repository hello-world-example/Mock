package xyz.kail.demo.junit5;

import org.junit.Before;
import org.junit.Test;

public class HelloTest4 {

    @Before
    public void beforeTest() {
        System.out.println("@Before");
    }

    @Test
    public void testOne() {
        System.out.println("测试1");
    }

}
