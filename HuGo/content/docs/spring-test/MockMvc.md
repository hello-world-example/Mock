# MockMvc



## 简介

- 



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







## Read More

- Spring Testing [3.7. MockMvc](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework)