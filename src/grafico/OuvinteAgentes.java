package grafico;

import java.awt.Color;

import jogo.CampoAgentesListener;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OuvinteAgentes implements CampoAgentesListener {

	private view.Campo campo;

	@Override
	public void jogadorIndoNaDirecaoDaBolaBemLoko(jogo.Jogador jogador) {
		campo.jogadorSeguirBola(jogador.getNome());
	}

	@Override
	public void jogadorAdicionado(String nomeJogador) {
		grafico.Jogador jogadorGrafico = new grafico.Jogador(nomeJogador, Color.RED);
		jogadorGrafico.setX(0);
		jogadorGrafico.setY(0);
		campo.addJogadorCasa(new Jogador());
	}

	@Override
	public void bolaEmJogo() {
	}

}
