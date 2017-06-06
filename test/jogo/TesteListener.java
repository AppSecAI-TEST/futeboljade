package jogo;

import java.awt.Point;

public class TesteListener extends JogoListenerAdapter {
	@Override
	public void jogadorMoveu(jogo.Jogador jogador) {
		System.out.printf("Jogador %s moveu ", jogador.getNome());
	}

	@Override
	public void jogadorIndoNaDirecaoDaBolaBemLoko(Jogador jogador, Point bola) {
		System.out.printf("Jogador indo %s na diração da bola", jogador.getNome());
	}
}