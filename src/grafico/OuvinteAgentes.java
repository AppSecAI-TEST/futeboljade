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

	@Override
	public void jogadorPegouBola(String nome) {
		campo.setJogadorComBola(campo.getJogador(nome));
	}
	
	@Override
	public void jogadorDeveParar(String nome) {
		campo.getJogador(nome).setVelocidade(0);
	}

	@Override
	public void jogadorDeveChutar(String nome, int erro, int aceleracao, int velocidade) {
		campo.jogadorComBolaChutarGol(erro, aceleracao, velocidade);
	}

}
