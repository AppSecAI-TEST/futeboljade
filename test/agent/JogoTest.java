package agent;

import jogo.Campo;
import org.junit.Test;

public class JogoTest {

	@Test
	public void adiciona_jogador() {
		Campo campo = new Campo();
		campo.adicionaJogador("Robinho", "Brasil");
	}

}
