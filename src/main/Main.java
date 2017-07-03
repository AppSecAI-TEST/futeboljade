package main;

import grafico.OuvinteAgentes;
import jogo.OuvinteParteGrafica;
import view.Estadio;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		Estadio estadio = new Estadio();		
		
		estadio.iniciar();
		estadio.setListener(() -> {
            jogo.Campo campo = new jogo.Campo();

            campo.addListener(new OuvinteAgentes(estadio.getCampo()));
            estadio.getCampo().addListener(new OuvinteParteGrafica(campo));
            campo.setBolaEmJogo(true);

            campo.adicionaJogador("1", "CASA");
            campo.adicionaJogador("2", "CASA");
            campo.adicionaJogador("3", "CASA");
            campo.adicionaJogador("4", "CASA");
            campo.adicionaJogador("5", "CASA");
            campo.adicionaJogador("6", "VISITANTE");
            campo.adicionaJogador("7", "VISITANTE");
            campo.adicionaJogador("8", "VISITANTE");
            campo.adicionaJogador("9", "VISITANTE");
            campo.adicionaJogador("10", "VISITANTE");

            campo.adicionaGoleiro("01", "CASA");
            campo.adicionaGoleiro("02", "VISITANTE");
        });
		
	}
}
