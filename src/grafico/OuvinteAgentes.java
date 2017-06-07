package grafico;

import java.awt.Color;

import jogo.CampoListener;
import lombok.AllArgsConstructor;
import view.Campo;

@AllArgsConstructor
public class OuvinteAgentes implements CampoListener {

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
