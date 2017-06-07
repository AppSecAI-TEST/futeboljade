package grafico;

import java.awt.Color;

import jogo.CampoAgentesListener;
import lombok.AllArgsConstructor;
import view.Campo;

@AllArgsConstructor
public class OuvinteAgentes implements CampoAgentesListener {

	private Campo campo;

	@Override
	public void jogadorIndoNaDirecaoDaBolaBemLoko(jogo.Jogador jogador) {
	}

	@Override
	public void jogadorAdicionado(jogo.Jogador jogador) {
		campo.addJogador(new grafico.Jogador(jogador.getNome(), Color.RED));
	}

	@Override
	public void bolaEmJogo() {
	}

}
