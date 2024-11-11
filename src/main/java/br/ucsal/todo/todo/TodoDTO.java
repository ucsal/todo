package br.ucsal.todo.todo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TodoDTO {


    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private Boolean completed;

}
