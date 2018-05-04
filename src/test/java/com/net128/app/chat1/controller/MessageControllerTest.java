package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Payload;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MessageService messageService;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void shouldReturnAllMessages() throws Exception {
        given(messageService.findUserMessages(null, null, null))
            .willReturn(Arrays.asList(
                    new Message("user1", "user2", new Payload("text1")),
                    new Message("user2", "user3", new Payload("text2"))
            ));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept","application/json");

        mvc.perform(get("/messages").headers(httpHeaders))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.content", hasSize(2)))
            .andExpect(jsonPath("$.content[0].senderId", is("user1")))
            .andExpect(jsonPath("$.content[0].recipientId", is("user2")))
            .andExpect(jsonPath("$.content[0].text", is("text1")))
            .andExpect(jsonPath("$.content[1].senderId", is("user2")))
            .andExpect(jsonPath("$.content[1].recipientId", is("user3")))
            .andExpect(jsonPath("$.content[1].text", is("text2")))
        ;
    }
}
