package jogo;

public interface CampoAgentesListener {

	void jogadorIndoNaDirecaoDaBolaBemLoko(jogo.Jogador jogador);

	void jogadorAdicionado(String nomeJogador, String time);

	void bolaEmJogo();

	void jogadorPegouBola(String nome);

	void jogadorDeveAtacar(String nome);

	void jogadorDeveDefender(String nome);

	void jogadorDeveChutar(String nome, MovimentoBola movimentoBola);

	void jogadorDevePassar(String passador, String recebedor);

	void goleiroAdicionado(String nome, String time);

    void informaEstado(String nome, String estado);

    void debuga(String nome, String mensagem);

    void informaPosicaoCampo(String nome, String posicao);

}
