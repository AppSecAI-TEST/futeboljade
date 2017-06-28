package jogo;

public interface CampoAgentesListener {

	void jogadorIndoNaDirecaoDaBolaBemLoko(jogo.Jogador jogador);

	void jogadorAdicionado(String nomeJogador, String time);

	void bolaEmJogo();

	void jogadorPegouBola(String nome);

	void jogadorDeveIrProAtaque(String nome);

	void jogadorDeveChutar(String nome, MovimentoBola movimentoBola);

	void jogadorDevePassar(String passador, String recebedor);

	void goleiroAdicionado(String nome, String time);

}
