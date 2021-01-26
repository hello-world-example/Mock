package xyz.kail.demo.mock.spring.web;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import xyz.kail.demo.mock.controller.HelloController;

import java.util.Locale;

public class WebMvcTest {

    @Test
    public void hello() throws Exception {
        final StandaloneMockMvcBuilder mvcBuilder = MockMvcBuilders.standaloneSetup(/*new HelloController()*/);

        final MockMvc mockMvc = mvcBuilder.build();

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/demo/hello/say/{hello}", "haha")
                .param("p1", "pppp1111")
                .queryParam("p1", "pppp2222")
                .locale(Locale.CHINA);


        final MvcResult mvcResult = mockMvc.perform(request).andReturn();

        final MockHttpServletResponse response = mvcResult.getResponse();

        final String contentType = response.getContentType();
        System.out.println(contentType);

        final String contentAsString = response.getContentAsString();

        final JsonPath jsonPath = JsonPath.compile("$.p1");
        final String p1 = JsonPath.parse(contentAsString).read(jsonPath, String.class);
        System.out.println(p1);

    }


}
