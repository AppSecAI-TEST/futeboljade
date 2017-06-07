package main;

import jogo.Campo;
import jogo.TesteListener;
import view.Estadio;

public class Main {
	public static void main(String args[]) {
		Campo campo = new Campo();
		campo.adicionaJogador("1", "A");
		campo.adicionaJogador("2", "A");

		campo.addListener(new TesteListener()); // TODO apagar
		Estadio estadio = new Estadio();
		campo.addListener(estadio.getCampo().getJogoListener());
		campo.setBolaEmJogo(true);
		estadio.iniciar();
	}
}
