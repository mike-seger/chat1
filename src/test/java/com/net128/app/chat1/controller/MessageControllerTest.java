package com.net128.app.chat1.controller;

import com.net128.app.chat1.model.Content;
import com.net128.app.chat1.model.Message;
import com.net128.app.chat1.service.MessageService;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
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
    private MessageService service;

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void shouldReturnAllMessages() throws Exception {
        given(service.findUserMessages(null, null, null))
            .willReturn(Arrays.asList(
                    new Message("user1", "user2", new Content("content1")),
                    new Message("user2", "user3", new Content("content2"))
            ));

        mvc.perform(get("/messages")).andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].senderId", is("user1")))
            .andExpect(jsonPath("$[0].recipientId", is("user2")))
            .andExpect(jsonPath("$[0].content.text", is("content1")))
            .andExpect(jsonPath("$[1].senderId", is("user2")))
            .andExpect(jsonPath("$[1].recipientId", is("user3")))
            .andExpect(jsonPath("$[1].content.text", is("content2")))
        ;
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
    public void shouldSaveUploadedMessage() throws Exception {
        Message message=new Message("user1", "user2", new Content("Hello uploaded message"));
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        mvc.perform(fileUpload("/messages").file(multipartFile)
                .param("message", message.toJson())
            )
            .andExpect(status().isOk());
        then(service).should().create(message, multipartFile);
    }
}
