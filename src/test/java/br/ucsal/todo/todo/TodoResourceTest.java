package br.ucsal.todo.todo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.ucsal.todo.util.NotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class TodoResourceTest {

	@InjectMocks
	private TodoResource todoResource;
	@Mock
	private TodoRepository todoRepository;
	@Mock
	private TodoService todoService;

	private TodoDTOBuilder todoDTOBuilder = new TodoDTOBuilder();

	@Test
	public void testGetAllTodos() throws Exception {
		List<TodoDTO> todos = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			TodoDTO todoDTO = new TodoDTO();
			todoDTO.setId((long) i);
			todoDTO.setTitle("Teste " + i);
			todoDTO.setCompleted(i % 2 == 0);
			todos.add(todoDTO);
		}

		when(todoService.findAll()).thenReturn(todos);

		// Executa o método
        ResponseEntity<List<TodoDTO>> response = todoResource.getAllTodos();

        // Verificações
        assertEquals(200, response.getStatusCode().value());
        assertEquals(5, response.getBody().size());
        assertEquals("Teste 1", response.getBody().get(0).getTitle());
        assertEquals(false, response.getBody().get(0).getCompleted());
	}

	@Test
	public void testGetAllTodosWhenDontExistTodo() throws Exception {
		List<TodoDTO> emptyTodos = new ArrayList<>();

		when(todoService.findAll()).thenReturn(emptyTodos);

		 // Executa o método
        ResponseEntity<List<TodoDTO>> response = todoResource.getAllTodos();

        // Verificações
        assertEquals(200, response.getStatusCode().value());
        assertEquals(0, response.getBody().size());
	}
	@Test
	public void testGetTodo() throws Exception {
		TodoDTO todoDto = todoDTOBuilder.id(1L).completed(true).title("Teste get todo").build();

		when(todoService.get(todoDto.getId())).thenReturn(todoDto);
		// Executa o método
        ResponseEntity<TodoDTO> response = todoResource.getTodo(1L);

        // Verificações
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Teste get todo", response.getBody().getTitle());
        assertEquals(true, response.getBody().getCompleted());
        // fazer verificações de campo por campo?
	}
	@Test
	public void testGetTodoWhenIdIsNotFound() throws Exception {
	    when(todoService.get(anyLong())).thenThrow(new NotFoundException());

	    assertThrows(NotFoundException.class, () -> {
	    	todoResource.getTodo(5214L); //id aleatório
        });
	    
	    
	}

	@Test
	public void testPostCreate() throws JsonProcessingException, Exception {
		TodoDTO todoDTO = todoDTOBuilder.completed(true).id(1L).title("Teste").build();
		when(todoService.create(todoDTO)).thenReturn(todoDTO.getId());
		
		ResponseEntity<Long> response = todoResource.createTodo(todoDTO);
		
		assertEquals(201, response.getStatusCode().value()); 
        assertEquals(1L, response.getBody());
		//to-do verificar o id que está retornando tbm
	}

	@Test
    public void testPostCreateWithAllFieldsNull() {
        TodoDTO todoDTO = new TodoDTO(); // Todos os campos nulos

        assertThrows(MethodArgumentNotValidException.class, () -> {
        	todoResource.createTodo(todoDTO);
        });

        verifyNoInteractions(todoService); 
    }

    @Test
    public void testPostCreateWithTitleEmpty() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle(""); // Título vazio
        todoDTO.setCompleted(null);

        assertThrows(MethodArgumentNotValidException.class, () -> {
        	todoResource.createTodo(todoDTO);
        });

        verifyNoInteractions(todoService); 
    }

    @Test
    public void testPostCreateWithTitleTooLong() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle("a".repeat(300)); 
        todoDTO.setCompleted(true);

        assertThrows(MethodArgumentNotValidException.class, () -> {
        	todoResource.createTodo(todoDTO);
        });

        verifyNoInteractions(todoService); 
    }

    @Test
    public void testPostCreateWithIdNull() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle("Test"); // Título válido
        todoDTO.setCompleted(true);
        todoDTO.setId(null); // ID nulo

        assertThrows(MethodArgumentNotValidException.class, () -> {
        	todoResource.createTodo(todoDTO);
        });

        verifyNoInteractions(todoService); 
    }

    @Test
    public void testPostCreateWithTitleNull() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(1L); 
        todoDTO.setCompleted(true);
        todoDTO.setTitle(null); 

        assertThrows(MethodArgumentNotValidException.class, () -> {
        	todoResource.createTodo(todoDTO);
        });

        verifyNoInteractions(todoService);
    }

    @Test
    public void testPostCreateWithCompletedNull() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(1L); // ID válido
        todoDTO.setTitle("Test"); // Título válido
        todoDTO.setCompleted(null); // Completed nulo

        assertThrows(MethodArgumentNotValidException.class, () -> {
        	todoResource.createTodo(todoDTO);
        });

        verifyNoInteractions(todoService); 
    }
	

	@Test
	public void testPut() throws JsonProcessingException, Exception {
		TodoDTO todoDTO = todoDTOBuilder.completed(true).id(1L).title("Teste").build();
		
        doNothing().when(todoService).update(anyLong(), any(TodoDTO.class));
        assertDoesNotThrow(() -> todoResource.updateTodo(1L, todoDTO));

        
        verify(todoService, times(1)).update(eq(1L), eq(todoDTO));
	}

	@Test
	public void testPutWhenIdDontExist() throws JsonProcessingException, Exception {
	    TodoDTO todoDTO = todoDTOBuilder.completed(true).id(1L).title("Teste").build();
        doThrow(new NotFoundException("Todo not found")).when(todoService).update(anyLong(), any(TodoDTO.class));
        NotFoundException exception = assertThrows(NotFoundException.class, () -> todoResource.updateTodo(999L, todoDTO));
      
        assertEquals("Todo not found", exception.getMessage());

        verify(todoService, times(1)).update(eq(999L), eq(todoDTO));
	}
//rever com mais cautela daqui pra baixo
	@Test
    public void testPutWhenFieldsAreNullOrBlankOrInvalid() {
        // Criação dos TodoDTOs para os testes
        TodoDTO todoDTOWithAllFieldsNull = new TodoDTO();
        TodoDTO todoDTOWithAllFieldsBlank = new TodoDTO();
        todoDTOWithAllFieldsBlank.setTitle(""); // Título em branco
        todoDTOWithAllFieldsBlank.setCompleted(null); // Completed nulo
        TodoDTO todoDTOWithTitleTooLong = new TodoDTO();
        todoDTOWithTitleTooLong.setTitle("a".repeat(300)); // Título com mais de 255 caracteres
        todoDTOWithTitleTooLong.setCompleted(true);

        doNothing().when(todoService).update(anyLong(), any(TodoDTO.class));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
        	todoResource.updateTodo(1L, todoDTOWithAllFieldsNull);
        });
        assertEquals("Fields cannot be null", exception.getMessage());

        exception = assertThrows(BadRequestException.class, () -> {
        	todoResource.updateTodo(1L, todoDTOWithAllFieldsBlank);
        });
        assertEquals("Title cannot be empty", exception.getMessage());

        exception = assertThrows(BadRequestException.class, () -> {
        	todoResource.updateTodo(1L, todoDTOWithTitleTooLong);
        });
        assertEquals("Title must not be longer than 255 characters", exception.getMessage());

        verify(todoService, times(3)).update(anyLong(), any(TodoDTO.class)); // Verifica 3 chamadas
    }
	
	@Test
	public void testDelete() {
	    Long todoId = 1L;

	    doNothing().when(todoService).delete(todoId);

	    todoService.delete(todoId);

	    verify(todoService, times(1)).delete(todoId);
	}
	@Test
	public void testDeleteWhenIdDontExist() {
	    Long todoId = 999L;

	    doThrow(new NotFoundException("Todo not found")).when(todoService).delete(todoId);

	    try {
	        todoService.delete(todoId);
	        fail("Expected NotFoundException to be thrown");
	    } catch (NotFoundException e) {
	        assertEquals("Todo not found", e.getMessage());
	    }
	    verify(todoService, times(1)).delete(todoId);
	}
}
