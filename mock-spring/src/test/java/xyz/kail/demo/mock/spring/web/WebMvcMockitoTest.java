package xyz.kail.demo.mock.spring.web;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import xyz.kail.demo.mock.controller.ExceptionControllerAdvice;
import xyz.kail.demo.mock.controller.HelloController;
import xyz.kail.demo.mock.filter.ParamFilter;
import xyz.kail.demo.mock.interceptor.ParamHandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// 使用 @Mock 注解
@RunWith(MockitoJUnitRunner.class)
public class WebMvcMockitoTest {

    @Mock
    private HelloController helloController;

    private MockMvc mockMvc;

    /**
     * 初始化 Mock
     */
    @Before
    public void before() {
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
