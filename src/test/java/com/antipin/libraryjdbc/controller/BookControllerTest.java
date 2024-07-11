package com.antipin.libraryjdbc.controller;

import com.antipin.libraryjdbc.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@AutoConfigureMockMvc
@Transactional
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/api/v1/books";

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenGetOneBookThenReturn200() throws Exception {
        mockMvc.perform(get(URL + "/100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("Title 1"))
                .andExpect(jsonPath("$.author").value("Author 1"))
                .andExpect(jsonPath("$.publicationYear").value(2002));
    }

    @Test
    public void whenGetAllThenReturn200() throws Exception {
        mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void whenPostThenCreateNewAndReturnWithLocation() throws Exception {
        Book newBook = new Book(null, "Created Title", "Created Author", 2020);
        MvcResult result= mockMvc.perform(post(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(newBook)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value(newBook.getTitle()))
                .andExpect(jsonPath("$.author").value(newBook.getAuthor()))
                .andReturn();
        Book createdBook = mapper.readValue(result.getResponse().getContentAsString(), Book.class);
        Assertions.assertThat(result.getResponse().getHeader("Location")).endsWith(String.valueOf(createdBook.getId()));
    }

    @Test
    public void whenPutThenUpdateCorrectly() throws Exception {
        Book bookToUpdate = new Book(100L, "Updated Title", "Updated Author", 2020);
        mockMvc.perform(put(URL)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookToUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(bookToUpdate.getId()))
                .andExpect(jsonPath("$.author").value(bookToUpdate.getAuthor()))
                .andExpect(jsonPath("$.title").value(bookToUpdate.getTitle()));
    }

    @Test
    public void whenDeleteThenReturn201AndDeleteEntity() throws Exception {
        String location = "/100";
        mockMvc.perform(delete(URL + location))
                .andDo(print())
                .andExpect(status().isNoContent());
        mockMvc.perform(get(URL + location))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
