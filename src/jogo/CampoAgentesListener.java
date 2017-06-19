package jogo;

public interface CampoAgentesListener {

	void jogadorIndoNaDirecaoDaBolaBemLoko(jogo.Jogador jogador);

	void jogadorAdicionado(String nomeJogador, String time);

	void bolaEmJogo();

	void jogadorPegouBola(String nome);

	void jogadorDeveParar(String nome);

	void jogadorDeveChutar(String nome, MovimentoBola movimentoBola);

}
