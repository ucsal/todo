package br.ucsal.todo.todo;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TodoResourceTest {

	@InjectMocks
	private TodoResource todoResource;
	@Mock
	private TodoRepository todoRepository;
	@Mock
	private TodoService todoService;
	
	@Autowired
	private ObjectMapper objectMapper;

	//get mapping lista
	//get mapping lista vazia
	//gte id - quando o id for encontrado
	//get id - quando id não for encontrado
	//post create 
	//post create - erros de validação
	//put 
	//put - id não encontrado
	//put - validações
	//delete 
	//delete - id não encontrado
}
