# Todo

Este app é um sistema de gerenciamento de tarefas. 
Aqui vão algumas dicas pra você trabalhar com o código e configurar o ambiente de desenvolvimento.

## Desenvolvimento

Atualize a conexão com o seu banco de dados local no arquivo `application.yml`
ou crie seu próprio `application-local.yml` pra ajustar as configurações durante o desenvolvimento.

Durante o desenvolvimento, é recomendado usar o perfil `local`. 
No IntelliJ, você pode adicionar `-Dspring.profiles.active=local` nas opções de VM da Configuração de Execução, 
depois de habilitar essa propriedade em “Modificar opções”.

Sua IDE precisa suportar o Lombok. 
- No IntelliJ, instale o plugin Lombok e habilite o processamento de anotações.

Depois de iniciar a aplicação, ela estará acessível em `localhost:8080`.

Uma versão api pode ser acessada por  `localhost:8080/swagger-ui.html`

## Build

Você pode fazer o `build` da aplicação usando o seguinte comando:

```
mvnw clean package
```
Inicie sua aplicação com o seguinte comando — aqui usando o perfil `production`:

```
java -Dspring.profiles.active=production -jar ./target/todo-0.0.1-SNAPSHOT.jar
```
Se precisar, uma imagem Docker pode ser criada com o plugin Spring Boot. 
Adicione `SPRING_PROFILES_ACTIVE=production` como variável de ambiente ao executar o container.

```
mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=br.ucsal/todo
```

## Leituras adicionais

* [Maven docs](https://maven.apache.org/guides/index.html)  
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)  
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Thymeleaf docs](https://www.thymeleaf.org/documentation.html)  
* [Bootstrap docs](https://getbootstrap.com/docs/5.3/getting-started/introduction/)  
* [Htmx in a nutshell](https://htmx.org/docs/)  
* [Learn Spring Boot with Thymeleaf](https://www.wimdeblauwe.com/books/taming-thymeleaf/)  
