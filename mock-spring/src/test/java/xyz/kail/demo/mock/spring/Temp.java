package xyz.kail.demo.mock.spring;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

public class Temp {

    @Test
    public void test() throws Exception {
        final StandaloneMockMvcBuilder mvcBuilder = MockMvcBuilders.standaloneSetup();
        final MockMvc mockMvc = mvcBuilder.build();

        mockMvc.perform(MockMvcRequestBuilders.get("/index"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200));



    }

}
