package main;

import grafico.OuvinteAgentes;
import jogo.OuvinteParteGrafica;
import view.Estadio;
import view.Estadio.Listener;

public class Main {
	public static void main(String args[]) throws InterruptedException {
		Estadio estadio = new Estadio();		
		
		estadio.iniciar();
		estadio.setListener(new Listener(){
			@Override
			public void iniciou() {
				jogo.Campo campo = new jogo.Campo();
				
				campo.addListener(new OuvinteAgentes(estadio.getCampo()));
				estadio.getCampo().addListener(new OuvinteParteGrafica(campo));
				campo.setBolaEmJogo(true);
				
				campo.adicionaJogador("1", "A");
				campo.adicionaJogador("2", "A");
			}
		});
		
	}
}
