package br.ucsal.todo.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import br.ucsal.todo.util.NotFoundException;
import jakarta.transaction.Transactional;

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
}

