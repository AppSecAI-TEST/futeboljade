package grafico;

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
	public void jogadorAdicionado(String nomeJogador, String time) {
		campo.addJogador(nomeJogador, time);
	}

	@Override
	public void bolaEmJogo() {
	}

}
