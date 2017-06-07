package main;

import view.Estadio;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		jogo.Campo campo = new jogo.Campo();
		campo.adicionaJogador("1", "A");
		campo.adicionaJogador("2", "A");
		Estadio estadio = new Estadio();
		campo.addListener(estadio.getCampo().getOuvinteAgentes());
		estadio.iniciar();
		campo.setBolaEmJogo(true);
	}
}
