package it.francescoforesti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescoforesti.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class MazeSolverResourceTest {
    @Autowired
    protected MockMvc restClient;

    @Autowired
    ObjectMapper om;

    @Test
    public void withExample1_returnsOk() throws Exception {
        String json = readFile("examples/example1.json");
        invokeController(json)
                .andExpect(status().isOk());
    }

    @Test
    public void withExample2_returnsOk() throws Exception {
        String json = readFile("examples/example2.json");
        invokeController(json)
                .andExpect(status().isOk());
    }

    @Test
    public void withMissingObjectsInInput_returnsBadRequest() throws Exception {
        String json = readFile("examples/bad_example_missing_objects.json");
        invokeController(json)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void withMissingStartRoomInInput_returnsBadRequest() throws Exception {
        String json = readFile("examples/bad_example_missing_start_room.json");
        invokeController(json)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void withUnsolvableInput_returnsUnprocessableEntity() throws Exception {
        String json = readFile("examples/bad_example_no_solution.json");
        invokeController(json)
                .andExpect(status().isUnprocessableEntity());
    }

    private ResultActions invokeController(String json) throws Exception {
        return restClient.perform(post("/v1/maze")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private String readFile(String name) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        StringBuilder stringBuilder = new StringBuilder();
        assert inputStream != null;
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        }
        return stringBuilder.toString();
    }
}
