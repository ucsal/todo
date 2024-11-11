package br.ucsal.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoResourceTeste {

	@Test
	public void testGetAll() {
		//arrange
		TodoDTO todoDTO = new TodoDTO();
		todoDTO.setCompleted(false);
		todoDTO.setTitle("Teste");
		//act & asssert
	}
}
