package agent;

import org.junit.Test;

import jogo.Campo;

public class JogoTest {

	@Test
	public void adiciona_jogador() {
		Campo campo = new Campo();
		campo.adicionaJogador("Robinho", "Brasil");
	}

}
