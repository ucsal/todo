package br.ucsal.todo.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.ucsal.todo.util.NotFoundException;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TodoServiceTest {

	@Autowired
	private TodoRepository todoRepository;
	@Autowired
	private TodoService todoService;
	
//    public TodoServiceTest(final TodoRepository todoRepository, final TodoService todoService) {
//        this.todoRepository = todoRepository;
//        this.todoService = todoService;
//    }
	
    @Test
	public void testFindAll() {
		//arrange
		List<TodoDTO> todosDTO = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Todo todo = new Todo();
            todo.setCompleted(false);
            todo.setTitle("Teste" + i);
            todoRepository.save(todo);
            
            TodoDTO todoDTO = new TodoDTO();
            todoDTO.setCompleted(false);
            todoDTO.setTitle("Teste"+i);
            todoDTO.setId(todo.getId());
            todosDTO.add(todoDTO);
        }
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
        todoRepository.save(todo);
        TodoDTO todoDTOExpected = new TodoDTO();
        todoDTOExpected.setCompleted(false);
        todoDTOExpected.setTitle("Teste");
        todoDTOExpected.setId(todo.getId());
        //act
        TodoDTO todoDTOActual = todoService.get(todo.getId());
        //assert
        assertEquals(todoDTOExpected, todoDTOActual);
        
    }
    
    @Test
    public void testGetWhenTodoDoesNotExist() {
    	//arrange && act && assert
    	assertThrows(NotFoundException.class, () -> todoService.get(1L));
    }
    
    @Test
    public void testCreate() {
    	//arrange
    	TodoDTO todoDTO = new TodoDTO();
    	todoDTO.setTitle("Teste");
    	todoDTO.setCompleted(false);

    	//act
    	
    	Long id = todoService.create(todoDTO);
    	//assert
    	assertTrue(id instanceof Long);
    }
    
    //esse teste vai falhar, pois não está retornando erro de validação
    //corrigir isso
    @Test
    public void testCreateWhenTitleIsNull() {
    	//arrange
    	TodoDTO todoDTO = new TodoDTO();
    	todoDTO.setCompleted(false);
    	todoDTO.setTitle(null);
    	//act && assert
    	assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTO));
    }
    @Test
    public void testCreateWhenTitleIsBiggerThan255() {
    	//arrange
    	TodoDTO todoDTO = new TodoDTO();
    	todoDTO.setCompleted(false);
    	todoDTO.setTitle("A".repeat(256));
    	//act && assert
    	assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTO));
    }
    
    @Test
    public void testCreatWhenTitleWhenCompletedIsNull() {
    	//arrange
    	TodoDTO todoDTO = new TodoDTO();
    	todoDTO.setCompleted(false);
    	todoDTO.setTitle("Teste");
    	//act && assert
    	assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTO));
    }

    
    @Test
    public void testUpdate() {
    	//arrange
    	Todo todo = new Todo();
    	todo.setTitle("Teste");
    	todo.setCompleted(true);
    	todoRepository.save(todo);
    	
    	TodoDTO todoDTO = new TodoDTO();
    	todoDTO.setId(todo.getId());
    	todoDTO.setCompleted(false);
    	todoDTO.setTitle("Teste2");
    	//act
    	todoService.update(todo.getId(), todoDTO);
    	//assert
    	Todo updatedTodo = todoRepository.findById(todo.getId()).get();
    	
    	assertEquals(false, updatedTodo.getCompleted());
    	assertEquals("Teste2", updatedTodo.getTitle());
    }
    
    //o teste abaixo necessita ser refinado
    @Test
    public void testUpdateWithInvalidFields() {
    	//arrange
    	Todo todo = new Todo();
    	todo.setTitle("Teste");
    	todo.setCompleted(true);
    	todoRepository.save(todo);
    	
    	TodoDTO todoDTO = new TodoDTO();
    	todoDTO.setId(todo.getId());
    	todoDTO.setCompleted(null);
    	todoDTO.setTitle("");
    	
    	assertThrows(ConstraintViolationException.class, () -> todoService.update(todo.getId(), todoDTO));
    }
    
    @Test
    public void testDelete() {
    	//arrange
    	Todo todo = new Todo();
    	todo.setCompleted(true);
    	todo.setTitle("Delete teste");
    	todoRepository.save(todo);
    	//act
    	
    	todoService.delete(todo.getId());
    	//assert
    	
    	Optional<Todo> findById = todoRepository.findById(todo.getId());
    	assertFalse(findById.isPresent());
    }
    
    @Test
    public void testDeleteWhenIdNotFound() {
    	Long id = 1L;
    	assertThrows(EntityNotFoundException.class, () -> todoService.delete(id));
    }
}

