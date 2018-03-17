package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Payload;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.model.UserContext;
import com.net128.app.chat1.service.MessageService;

import java.util.Arrays;

import com.net128.app.chat1.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private UserService userService;

    //TODO make tests parameterized with all elevations - affecting sender too
    private final static UserContext userContext=new UserContext(false, "senderId");

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void shouldReturnAllMessages() throws Exception {
        given(messageService.findUserMessages(userContext, null, null, null))
            .willReturn(Arrays.asList(
                    new Message("user1", "user2", new Payload("text1")),
                    new Message("user2", "user3", new Payload("text2"))
            ));

        given(userService.getUserContext(Matchers.<HttpServletRequest>any())).willReturn(userContext);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept","application/json");

        mvc.perform(get("/messages").headers(httpHeaders))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].senderId", is("user1")))
            .andExpect(jsonPath("$[0].recipientId", is("user2")))
            .andExpect(jsonPath("$[0].payload.text", is("text1")))
            .andExpect(jsonPath("$[1].senderId", is("user2")))
            .andExpect(jsonPath("$[1].recipientId", is("user3")))
            .andExpect(jsonPath("$[1].payload.text", is("text2")))
        ;
    }
}
