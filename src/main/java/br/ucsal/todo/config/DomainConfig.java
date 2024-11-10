package br.ucsal.todo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("br.ucsal.todo")
@EnableJpaRepositories("br.ucsal.todo")
@EnableTransactionManagement
public class DomainConfig {
}
