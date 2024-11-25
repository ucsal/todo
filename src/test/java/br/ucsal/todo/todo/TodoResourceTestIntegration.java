package br.ucsal.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoResourceTestIntegration {

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAll() throws Exception {
		// arrange
		for (int i = 0; i < 5; i++) {
			Todo todo = new Todo();
			todo.setCompleted(false);
			todo.setTitle("a");
			todoRepository.save(todo);
		}
		// act & asssert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("a"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(5));
	}

	@Test
	public void testGetById() throws Exception {
		// arrange
		Todo todo = new Todo();
		todo.setCompleted(false);
		todo.setTitle("a");
		todoRepository.save(todo);
		// act & asssert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/todos/" + todo.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("a"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(false));
	}

	@Test
	public void testGetByIdNotFound() throws Exception {
		// act && assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/todos/" + 1))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testPost() throws JsonProcessingException, Exception {
		// arrange
		TodoDTO todoDTO = new TodoDTO();
		todoDTO.setCompleted(false);
		todoDTO.setTitle("Teste Create");
		// act & assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/todos").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(todoDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	// testar as validações
	// I'm trying to say that I stopped here because, you know, I need to think
}
