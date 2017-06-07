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
	public void jogadorAdicionado(jogo.Jogador jogador) {
		System.out.println("Adicionado");
		grafico.Jogador jogadorGrafico = new grafico.Jogador(jogador.getNome(), Color.RED);
		jogadorGrafico.setX(0);
		jogadorGrafico.setY(0);
		campo.addJogador(jogadorGrafico);
	}

	@Override
	public void bolaEmJogo() {
	}

}
