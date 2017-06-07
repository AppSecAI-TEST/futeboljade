package jogo;

import lombok.AllArgsConstructor;
import view.CampoGraficoListener;

@AllArgsConstructor
public class OuvinteParteGrafica implements CampoGraficoListener {

	private final jogo.Campo campo;
	
	@Override
	public void jogadorColidiuComBola(String nome) {
		campo.jogadorColidiuComBola(nome);
	}

}
