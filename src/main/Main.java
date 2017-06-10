package main;

import grafico.OuvinteAgentes;
import jogo.OuvinteParteGrafica;
import view.Estadio;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		Estadio estadio = new Estadio();		
		
		estadio.iniciar();
		estadio.setListener(new Estadio.Listener(){
			@Override
			public void iniciou() {
				jogo.Campo campo = new jogo.Campo();
				
				campo.addListener(new OuvinteAgentes(estadio.getCampo()));
				estadio.getCampo().addListener(new OuvinteParteGrafica(campo));
				campo.setBolaEmJogo(true);
				
				campo.adicionaJogador("1", "CASA");
				campo.adicionaJogador("2", "VISITANTE");
			}
		});
		
	}
}
