# 常用注解对比



> **注意**： 注意包名，不要混用



|    JUnit4    |       JUnit5       | 说明                                                         |
| :----------: | :----------------: | :----------------------------------------------------------- |
|    @Test     |      `@Test`       | 测试方法，JUnit5 @Test 注解没有任何属性（`timeout`、`expected`） |
| @BeforeClass |    `@BeforeAll`    | **所有**测试用例**之前**执行                                 |
|   @Before    |   `@BeforeEach`    | **每个**测试用例**之前**执行，即： @Test、@RepeatedTest、@ParameterizedTest、@TestFactory |
|    @After    |    `@AfterEach`    | **每个**测试用例**之后**执行，即： @Test、@RepeatedTest、@ParameterizedTest、@TestFactory |
| @AfterClass  |    `@AfterAll`     | **所有**测试用例**之后**执行                                 |
|              |                    |                                                              |
|   @Ignore    |     @Disabled      | 禁用一个测试类或测试方法                                     |
|  @Category   |        @Tag        | 标记和过滤                                                   |
| @Parameters  | @ParameterizedTest | 参数化测试                                                   |
|              |                    |                                                              |
| **@RunWith** |   `@ExtendWith`    | 用来确定这个类怎么运行的                                     |
|    @Rule     |   `@ExtendWith`    | Rule 是一组实现了 TestRule 接口的共享类，提供了验证、监视TestCase和外部资源管理等能力 |
|  @ClassRule  |   `@ExtendWith`    | @ClassRule 用于测试类中的静态变量，必须是 TestRule 接口的实例，且访问修饰符必须为 public |
|              |                    |                                                              |
|              |  **@DisplayName**  | 测试类或方法的显示名称                                       |
|              |     **@Neste**     | 嵌套测试                                                     |
|              |   @RepeatedTest    | 重复测试                                                     |
|              |    @TestFactory    | 动态测试                                                     |



## 一个示例

```java

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
```







## Read More

- [JUnit4 与 JUnit 5 常用注解对比](https://blog.csdn.net/winteroak/article/details/80591598)
- [JUnit5 系列索引](https://blog.csdn.net/ryo1060732496/article/details/80792246)