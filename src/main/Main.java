package main;

import java.awt.Point;

import jogo.Campo;
import jogo.TesteListener;
import view.Estadio;

public class Main {
	public static void main(String args[]) {
		Campo campo = new Campo();
		campo.adicionaJogador("1", "A", new Point(0, 0));

		campo.addListener(new TesteListener()); // TODO apagar
		Estadio estadio = new Estadio();
		campo.addListener(estadio.getCampo().getJogoListener());
		campo.setBolaEmJogo(true);
		
		estadio.iniciar();
	}
}
