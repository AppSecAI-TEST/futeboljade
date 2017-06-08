package main;

import grafico.OuvinteAgentes;
import jogo.OuvinteParteGrafica;
import view.Estadio;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		jogo.Campo campo = new jogo.Campo();
		Estadio estadio = new Estadio();
		
		campo.addListener(new OuvinteAgentes(estadio.getCampo()));
		estadio.getCampo().addListener(new OuvinteParteGrafica(campo));
		
		estadio.iniciar();
		campo.setBolaEmJogo(true);
		
		campo.adicionaJogador("1", "A");
		//campo.adicionaJogador("2", "A");
	}
}
