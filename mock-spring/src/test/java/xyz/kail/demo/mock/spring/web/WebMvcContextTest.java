package xyz.kail.demo.mock.spring.web;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import xyz.kail.demo.mock.MockApplication;
import xyz.kail.demo.mock.controller.HelloController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MockApplication.class)
public class WebMvcContextTest {

    @Resource
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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
