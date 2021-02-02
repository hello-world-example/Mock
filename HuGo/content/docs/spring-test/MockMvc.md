# MockMvc



## 简介

- **MockMvc** 不需要启动 Web 容器
- 测试方式基于 **单元测试**  和 **集成测试** 之间



## 静态导入

```java
// 构造 MockMvc 测试环境，如：增加 Filter、Interceptors、ControllerAdvice 等
import org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

// 创建 请求信息，如：url、header、cookie 等
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// Assert 断言操作，如：MockMvcResultMatchers.status().is(200) 断言
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// Mock Response 处理方式，如： MockMvcResultHandlers.print() 打印响应信息到控制台
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
```



## 主要流程

- `MockMvcBuilders.*` 创建 `MockMvc` 
- `mockMvc.perform(RequestBuilder)` 执行 `MockMvcRequestBuilders.*` 构造的请求信息
- `MockMvcResultMatchers.*` 对响应结果进行断言



## standaloneSetup 方式

> 该方法更接近与 **单元测试**

```java
MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
```



如果只是为了测试 `Filter`、`Interceptors` 等，不测试 `HelloController` 逻辑，可以 Mock  `HelloController`

```java
// 使用 @Mock 注解
@RunWith(MockitoJUnitRunner.class)
public class WebMvcMockitoTest {

  @Mock
  private HelloController helloController;

  private MockMvc mockMvc;

  /**
   * 初始化 Mock 逻辑
   */
  @Before
  public void before() {
    //
    // 当参数是 "p" 的时候，返回 {"p1":"p"}
    Mockito.when(helloController.index("w", "p"))
      .thenReturn(Collections.singletonMap("p1", "p"));

    // 当参数是 "ex" 的时候，抛出 IllegalArgumentException 异常
    Mockito.when(helloController.index("w", "ex"))
      .thenThrow(new IllegalArgumentException("参数错误"));

    mockMvc = MockMvcBuilders.standaloneSetup(helloController)
      // Filter
      .addFilter(new ParamFilter())
      // Interceptor
      .addInterceptors(new ParamHandlerInterceptor())
      // ControllerAdvice
      .setControllerAdvice(new ExceptionControllerAdvice())
      .build();
  }

  @Test
  public void testSuccess() throws Exception {
    final MockHttpServletRequestBuilder request = get("/demo/hello/say/{world}", "w")
      .param("p1", "p");

    mockMvc.perform(request)
      // 打印详细信息
      .andDo(MockMvcResultHandlers.print())
      // 返回校验
      .andExpect(MockMvcResultMatchers.status().is(200))
      .andExpect(MockMvcResultMatchers.jsonPath("$.p1", Matchers.equalTo("p")))
      .andExpect(MockMvcResultMatchers.header().string("Content-Type", "application/json"));
  }

  @Test
  public void testException() throws Exception {
    final MockHttpServletRequestBuilder request = get("/demo/hello/say/{world}", "w")
      .param("p1", "ex")
      .characterEncoding(StandardCharsets.UTF_8.name());

    mockMvc.perform(request)
      // 设置 Response 编码
      .andDo(result -> result.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name()))
      .andDo(MockMvcResultHandlers.print())
      // 返回校验
      .andExpect(MockMvcResultMatchers.status().is(200))
      .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.equalTo("500")))
      ;
  }

}
```



## webAppContextSetup 方式

该方式会加载完整的 Spring 配置

```java
// wac : WebApplicationContext
MockMvcBuilders.webAppContextSetup(wac).build();
```

关键是构造 `WebApplicationContext` 实例

### 方式 1 

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class WebMvcContextTest {

  @Resource
  private WebApplicationContext wac;

}
```

### 方式 2 自定义扫描类

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = HelloController.class)
public class WebMvcContextTest {

    @Resource
    private WebApplicationContext wac;

    @Resource
    private HelloController helloController;
}
```

### 方式 3 xml 方式

```java
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
public class WebMvcContextTest {

    @Resource
    private WebApplicationContext wac;

}
```

**applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://www.springframework.org/schema/context
                           http://www.springframework.org/schema/context/spring-context.xsd"
>

    <context:annotation-config/>
    <context:component-scan base-package="xyz.kail.demo"/>

</beans>
```

### 方式4 Spring Boot 方式

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockApplication.class)
public class WebMvcContextTest {
  
  @Resource
  private WebApplicationContext wac;
  
}
```



## Read More

- Spring Testing [3.7. MockMvc](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework)
- [官方测试用例](https://github.com/spring-projects/spring-framework/tree/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples)