package xyz.kail.demo.junit5;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Calculator")
public class CalculatorTest {

    @BeforeAll
    public static void init() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach
    public void create() {
        System.out.println("@BeforeEach");
    }

    @Nested
    class AddTest {

        @Test
        @DisplayName("Test 1 + 2 = 3")
        public void testAdd() {
            assertEquals(3, 1 + 2);
        }

        @Test
        @DisplayName("Test -1 + 2 = 1")
        public void testAdd2() {
            assertEquals(1, -1 + 2);
        }
    }


    @RepeatedTest(5)
    @DisplayName("RepeatedTest 3 - 2 = 1")
    public void testSubtract() {
        assertEquals(1, 3 - 2);
    }

    @Test
    @Disabled
    @DisplayName("disabled test")
    public void ignoredTest() {
        System.out.println("This test is disabled");
    }

    @AfterEach
    public void destroy() {
        System.out.println("@AfterEach");
    }

    @AfterAll
    public static void cleanup() {
        System.out.println("@AfterAll");
    }
}