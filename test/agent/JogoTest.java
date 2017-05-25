package agent;

import org.junit.Test;

public class JogoTest {

	@Test
	public void adiciona_jogador() {
		Jogo jogo = new Jogo();
		jogo.adicionaJogador("Robinho");
	} 
}
