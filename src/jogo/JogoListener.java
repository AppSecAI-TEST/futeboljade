package jogo;

import java.awt.Point;

public interface JogoListener {

	public void jogadorMoveu(jogo.Jogador jogador);

	public void jogadorIndoNaDirecaoDaBolaBemLoko(Jogador jogador, Point bola);

}
