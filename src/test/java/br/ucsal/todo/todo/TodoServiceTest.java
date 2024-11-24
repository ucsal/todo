package br.ucsal.todo.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import jakarta.validation.ConstraintViolationException;

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
		// arrange
		List<Todo> todos = new ArrayList<>();
		List<TodoDTO> todosDTO = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			Todo todo = new Todo();
			todo.setCompleted(false);
			todo.setTitle("Teste" + i);
			todos.add(todo);
			TodoDTO todoDTO = new TodoDTO();
			todoDTO.setCompleted(false);
			todoDTO.setTitle("Teste" + i);
			todoDTO.setId(todo.getId());
			todosDTO.add(todoDTO);
		}
		// mock
		when(todoRepository.findAll(Sort.by("id"))).thenReturn(todos);
		// act
		List<TodoDTO> findAll = todoService.findAll();
		// assert
		assertEquals(todosDTO, findAll);
	}

	@Test
	public void testFindAllWhenDontExistTodo() {
		// arrange
		List<Todo> todos = new ArrayList<>();
		List<TodoDTO> todosDTO = new ArrayList<>();
		// mock
		when(todoRepository.findAll(Sort.by("id"))).thenReturn(todos);
		// act
		List<TodoDTO> findAll = todoService.findAll();
		// assert
		assertEquals(todosDTO, findAll);
		assertTrue(todosDTO.isEmpty());
	}

	@Test
	public void testGetWhenTodoExist() {
		// arrange
		Todo todo = new Todo();
		todo.setCompleted(false);
		todo.setTitle("Teste");
		todo.setId(1L);
		Optional<Todo> optionalTodo = Optional.of(todo);

		TodoDTO todoDTOExpected = new TodoDTO();
		todoDTOExpected.setCompleted(false);
		todoDTOExpected.setTitle("Teste");
		todoDTOExpected.setId(todo.getId());
		// mock
		when(todoRepository.findById(1L)).thenReturn(optionalTodo);

		// act
		TodoDTO todoDTOActual = todoService.get(todo.getId());
		// assert
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
		// arrange
		TodoDTO todoDTO = new TodoDTO();
		todoDTO.setTitle("Teste Tarefa");
		todoDTO.setCompleted(false);

		Todo todo = new Todo();
		todo.setId(1L);
		todo.setTitle("Teste Tarefa");
		todo.setCompleted(false);

		// mock
		when(todoRepository.save(any(Todo.class))).thenReturn(todo);

		// act
		Long createdId = todoService.create(todoDTO);

		// assert
		assertNotNull(createdId);
		assertEquals(1L, createdId);
		verify(todoRepository, times(1)).save(any(Todo.class));
	}

	@Test
	public void testCreateWhenFieldsAreNullOrBlankOrInvalid() {
		// arrange
		TodoDTOBuilder todoDTOBuilder = new TodoDTOBuilder();
		// Objeto com todos os campos nulos
		TodoDTO todoDTOWithAllFieldsNull = todoDTOBuilder.idNull().titleNull().completedNull().build();
		TodoDTO todoDTOWithAllFieldsNullWithCompletedBlank = todoDTOBuilder.idNull().title("").completedNull().build();
		TodoDTO todoDTOWithAllFieldsNullWithCompletedBiggerThan255 = todoDTOBuilder.id(1L).title("a".repeat(300))
				.completed(true).build();
		TodoDTO todoDTOWithIdNull = todoDTOBuilder.idNull().title("Test").completed(true).build();
		TodoDTO todoDTOWithTitleNull = todoDTOBuilder.id(1L).titleNull().completed(true).build();
		TodoDTO todoDTOWithCompletedNull = todoDTOBuilder.id(1L).title("Test").completedNull().build();

		assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTOWithAllFieldsNull));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.create(todoDTOWithAllFieldsNullWithCompletedBlank));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.create(todoDTOWithAllFieldsNullWithCompletedBiggerThan255));
		assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTOWithIdNull));
		assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTOWithTitleNull));
		assertThrows(ConstraintViolationException.class, () -> todoService.create(todoDTOWithCompletedNull));
		// eu preciso mockar o comportamento do .save()?
	}

	@Test
	public void testUpdate() {

		// arrange
		TodoDTO todoDTO = new TodoDTO();
		todoDTO.setId(10L);
		todoDTO.setTitle("Teste Tarefa");
		todoDTO.setCompleted(false);

		Todo todo = new Todo();
		todo.setId(10L);
		todo.setTitle("Teste Tarefa");
		todo.setCompleted(false);

		Optional<Todo> optionalTodo = Optional.of(todo);

		// mock
		when(todoRepository.findById(1L)).thenReturn(optionalTodo);
		when(todoRepository.save(any(Todo.class))).thenReturn(todo);
		// act
		todoService.update(1L, todoDTO);

		// assert
		verify(todoRepository, times(1)).findById(1L); // Verifica se o findById foi chamado uma vez
		verify(todoRepository, times(1)).save(any(Todo.class));
	}

	@Test
	public void testUpdateWhenIdDontExists() {
		// arrange
		TodoDTOBuilder todoDTOBuilder = new TodoDTOBuilder();
		TodoDTO todoDTO = todoDTOBuilder.id(1L).completed(true).title("Teste").build();
		// mock
		when(todoRepository.findById(1L)).thenReturn(Optional.empty());
		// assert
		assertThrows(NotFoundException.class, () -> todoService.update(todoDTO.getId(), todoDTO));
		verify(todoRepository, times(1)).findById(1L);
	}

	@Test
	public void testUpateWhenFieldsAreNullOrBlankOrInvalid() {
		// arrange
		TodoDTOBuilder todoDTOBuilder = new TodoDTOBuilder();
		// Objeto com todos os campos nulos
		TodoDTO todoDTOWithAllFieldsNull = todoDTOBuilder.idNull().titleNull().completedNull().build();
		TodoDTO todoDTOWithAllFieldsNullWithCompletedBlank = todoDTOBuilder.idNull().title("").completedNull().build();
		TodoDTO todoDTOWithAllFieldsNullWithCompletedBiggerThan255 = todoDTOBuilder.id(1L).title("a".repeat(300))
				.completed(true).build();
		TodoDTO todoDTOWithIdNull = todoDTOBuilder.idNull().title("Test").completed(true).build();
		TodoDTO todoDTOWithTitleNull = todoDTOBuilder.id(1L).titleNull().completed(true).build();
		TodoDTO todoDTOWithCompletedNull = todoDTOBuilder.id(1L).title("Test").completedNull().build();

		assertThrows(ConstraintViolationException.class,
				() -> todoService.update(todoDTOWithAllFieldsNull.getId(), todoDTOWithAllFieldsNull));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.update(todoDTOWithAllFieldsNullWithCompletedBlank.getId(),
						todoDTOWithAllFieldsNullWithCompletedBlank));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.update(todoDTOWithAllFieldsNullWithCompletedBiggerThan255.getId(),
						todoDTOWithAllFieldsNullWithCompletedBiggerThan255));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.update(todoDTOWithIdNull.getId(), todoDTOWithIdNull));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.update(todoDTOWithTitleNull.getId(), todoDTOWithTitleNull));
		assertThrows(ConstraintViolationException.class,
				() -> todoService.update(todoDTOWithCompletedNull.getId(), todoDTOWithCompletedNull));
		// eu preciso mockar o comportamento do .save()?
	}
	
	@Test
    void testDelete() {
        verify(todoRepository, times(1)).deleteById(1L);
    }
}
