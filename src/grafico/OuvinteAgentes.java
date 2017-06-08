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
		jogador.getTime().getNome();
		campo.addJogador(jogador.getNome(), jogador.getTime().getNome());
	}

	@Override
	public void bolaEmJogo() {
	}

}
