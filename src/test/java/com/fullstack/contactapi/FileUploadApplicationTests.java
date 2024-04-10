package com.fullstack.contactapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class FileUploadApplicationTests {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private ContactController contactController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    void shouldUploadPhoto() throws Exception {
        final String id = "37bfd772-5208-4627-9ddf-d0d9f01485f5";
        Resource resource = new ClassPathResource("test.jpg");

        Resource fileResource = new FileSystemResource(resource.getFile());
        assertNotNull(fileResource);

        MockMultipartFile firstFile = new MockMultipartFile(
                "attachments", fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream());

        assertNotNull(firstFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        /*MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", inputStream);*/

       /* HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);*/
/*
        String serverUrl = "http://localhost:8080/contactapi/contacts/photo";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.PUT, requestEntity, String.class);
                //.postForEntity(serverUrl, requestEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);*/

        MockHttpServletRequestBuilder builder =
            MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/contacts/photo")
                .file("file", firstFile.getBytes())
                .param("id", id)
                .headers(headers);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();


        /*this.mockMvc.perform(multipart(HttpMethod.PUT, "/contacts/photo")
                        .file("file", firstFile.getBytes())
                        .param("id", id)
                        .headers(headers))
                .andExpect(content().string(""));*/
    }
}
