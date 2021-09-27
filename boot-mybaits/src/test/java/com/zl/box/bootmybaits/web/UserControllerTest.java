package com.zl.box.bootmybaits.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zl.box.bootmybaits.common.utils.RandomValue;
import com.zl.box.bootmybaits.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getUser() throws Exception {

    }

    @Test
    public void findAll() {
    }

    @Test
    public void save() throws Exception{
        final ArrayList<User> users = new ArrayList<>(100);
        for (int i = 0; i <10 ; i++) {
            final User user = new User();
            if(i%2==0){
                user.setSex("男");
            }else{
                user.setSex("女");
            }
            user.setAddress(RandomValue.getAddress());
            user.setBirthday(new Date());
            user.setUsername(RandomValue.getChineseName());
            users.add(user);
        }

        System.out.println(users.size());
        users.stream().distinct().forEach( System.out::println );
        System.out.println( users.stream().distinct().count() );

        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(users);
        MvcResult result1 = mockMvc
                .perform(MockMvcRequestBuilders.post("/user/save").content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print()).andReturn();
        assertEquals(result1.getResponse().getStatus(), HttpServletResponse.SC_OK);
    }



}
