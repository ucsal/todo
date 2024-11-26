package br.ucsal.todo.todo;

public class TodoDTOBuilder {

    private Long id;
    private String title;
    private Boolean completed;
    
    public TodoDTOBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TodoDTOBuilder title(String title) {
        this.title = title;
        return this;
    }

    public TodoDTOBuilder completed(Boolean completed) {
        this.completed = completed;
        return this;
    }
    public TodoDTOBuilder idNull() {
        this.id = null;
        return this;
    }

    public TodoDTOBuilder titleNull() {
        this.title = null;
        return this;
    }

    public TodoDTOBuilder completedNull() {
        this.completed = null;
        return this;
    }
    // Método para construir a instância de TodoDTO
    public TodoDTO build() {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(this.id);
        todoDTO.setTitle(this.title);
        todoDTO.setCompleted(this.completed);
        return todoDTO;
    }
}
