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

            campo.adicionaGoleiro("01", "CASA");

            campo.adicionaJogador("02", "CASA");
            campo.adicionaJogador("03", "CASA");
            campo.adicionaJogador("04", "CASA");
            campo.adicionaJogador("05", "CASA");
            campo.adicionaJogador("06", "CASA");
            campo.adicionaJogador("07", "CASA");

            campo.adicionaGoleiro("11", "VISITANTE");

            campo.adicionaJogador("12", "VISITANTE");
            campo.adicionaJogador("13", "VISITANTE");
            campo.adicionaJogador("14", "VISITANTE");
            campo.adicionaJogador("15", "VISITANTE");
            campo.adicionaJogador("16", "VISITANTE");
            campo.adicionaJogador("17", "VISITANTE");

        });
		
	}
}
