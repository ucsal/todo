package br.ucsal.todo.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.ucsal.todo.util.NotFoundException;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoServiceTest {

	
//	@Autowired
//	private TodoService todoService;
	
	@InjectMocks
    private TodoService todoService;
	@Mock
	private TodoRepository todoRepository;
	
    @Test
	public void testFindAll() {
		//arrange
    	List<Todo> todos = new ArrayList<>();
		List<TodoDTO> todosDTO = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Todo todo = new Todo();
            todo.setCompleted(false);
            todo.setTitle("Teste" + i);
            todos.add(todo);
            
            TodoDTO todoDTO = new TodoDTO();
            todoDTO.setCompleted(false);
            todoDTO.setTitle("Teste"+i);
            todoDTO.setId(todo.getId());
            todosDTO.add(todoDTO);
        }
        
        //mock
        when(todoRepository.findAll(Sort.by("id"))).thenReturn(todos);
        //act
        List<TodoDTO> findAll = todoService.findAll();
        //assert
        assertEquals(todosDTO, findAll);
	}
    
    @Test
    public void testGetWhenTodoExist() {
    	//arrange
    	Todo todo = new Todo();
        todo.setCompleted(false);
        todo.setTitle("Teste");
        todo.setId(1L);
        Optional<Todo> optionalTodo = Optional.of(todo);
        
        TodoDTO todoDTOExpected = new TodoDTO();
        todoDTOExpected.setCompleted(false);
        todoDTOExpected.setTitle("Teste");
        todoDTOExpected.setId(todo.getId());
        //mock
        when(todoRepository.findById(1L)).thenReturn(optionalTodo);
        
        //act
        TodoDTO todoDTOActual = todoService.get(todo.getId());
        //assert
        assertEquals(todoDTOExpected, todoDTOActual);
    }
    
    @Test
    public void testGetWhenTodoDoesNotExist() {
    	//mock
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
    	//arrange && act && assert
    	assertThrows(NotFoundException.class, () -> todoService.get(1L));
    }
    
    @Test
    public void testCreate() {
    	//arrange
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle("Teste Tarefa");
        todoDTO.setCompleted(false);

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Teste Tarefa");
        todo.setCompleted(false);

        //mock
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        //act
        Long createdId = todoService.create(todoDTO);

        //assert
        assertNotNull(createdId);
        assertEquals(1L, createdId);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }
//    
//    //esse teste vai falhar, pois não está retornando erro de validação
//    //corrigir isso
//    @Test
//    public void testCreateWhenTitleIsNull() {
//    	//arrange
//    	TodoDTO todoDTO = new TodoDTO();
//    	todoDTO.setCompleted(false);
//    	todoDTO.setTitle(null);
//    	//act && assert
//    	assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTO));
//    }
//    @Test
//    public void testCreateWhenTitleIsBiggerThan255() {
//    	//arrange
//    	TodoDTO todoDTO = new TodoDTO();
//    	todoDTO.setCompleted(false);
//    	todoDTO.setTitle("A".repeat(256));
//    	//act && assert
//    	assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTO));
//    }
//    
//    @Test
//    public void testCreatWhenTitleWhenCompletedIsNull() {
//    	//arrange
//    	TodoDTO todoDTO = new TodoDTO();
//    	todoDTO.setCompleted(false);
//    	todoDTO.setTitle("Teste");
//    	//act && assert
//    	assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTO));
//    }
//
//    
//    @Test
//    public void testUpdate() {
//    	//arrange
//    	Todo todo = new Todo();
//    	todo.setTitle("Teste");
//    	todo.setCompleted(true);
//    	todoRepository.save(todo);
//    	
//    	TodoDTO todoDTO = new TodoDTO();
//    	todoDTO.setId(todo.getId());
//    	todoDTO.setCompleted(false);
//    	todoDTO.setTitle("Teste2");
//    	//act
//    	todoService.update(todo.getId(), todoDTO);
//    	//assert
//    	Todo updatedTodo = todoRepository.findById(todo.getId()).get();
//    	
//    	assertEquals(false, updatedTodo.getCompleted());
//    	assertEquals("Teste2", updatedTodo.getTitle());
//    }
//    
//    //o teste abaixo necessita ser refinado
//    @Test
//    public void testUpdateWithInvalidFields() {
//    	//arrange
//    	Todo todo = new Todo();
//    	todo.setTitle("Teste");
//    	todo.setCompleted(true);
//    	todoRepository.save(todo);
//    	
//    	TodoDTO todoDTO = new TodoDTO();
//    	todoDTO.setId(todo.getId());
//    	todoDTO.setCompleted(null);
//    	todoDTO.setTitle("");
//    	
//    	assertThrows(ConstraintViolationException.class, () -> todoService.update(todo.getId(), todoDTO));
//    }
//    
//    @Test
//    public void testDelete() {
//    	//arrange
//    	Todo todo = new Todo();
//    	todo.setCompleted(true);
//    	todo.setTitle("Delete teste");
//    	todoRepository.save(todo);
//    	//act
//    	
//    	todoService.delete(todo.getId());
//    	//assert
//    	
//    	Optional<Todo> findById = todoRepository.findById(todo.getId());
//    	assertFalse(findById.isPresent());
//    }
//    
//    @Test
//    public void testDeleteWhenIdNotFound() {
//    	Long id = 1L;
//    	assertThrows(EntityNotFoundException.class, () -> todoService.delete(id));
//    }
}

