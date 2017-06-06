package main;

import java.awt.Point;

import jogo.Campo;
import jogo.TesteListener;

public class Main {
	public static void main(String args[]) {
		Campo campo = new Campo();
		campo.adicionaJogador("1", "A", new Point(0, 0));

		campo.addListener(new TesteListener()); // TODO apagar

		campo.bolaEmJogo();
	}
}
