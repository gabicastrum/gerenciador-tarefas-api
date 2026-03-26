package com.gerenciador.tarefas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class TarefasApplicationTests {

	@Test
	@DisplayName("Deve carregar o contexto da aplicação e executar o método main")
	void contextLoads() {
		assertDoesNotThrow(() -> TarefasApplication.main(new String[] {}));
	}
}
