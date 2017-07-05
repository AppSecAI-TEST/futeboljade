package jogo.estilojogo;

import jogo.Jogador;

import java.util.List;

public interface EstiloDeJogo {

	boolean deveChutar();

	int calculaErroDirecaoChute();

	int calculaAceleracaoChute();

	int calculaVelocidadeChute();

	boolean devePassar();

	Jogador selecionaColegaPassarBola(List<Jogador> stream);

	void movimentaComBola(Jogador jogador);

    boolean deveCorrerAtrasDaBola(Jogador jogador);

    void setPosicaoCampo(Jogador.PosicaoCampo posicaoCampo);

	void incrementaNumeroRespostasDistanciaBola();

	void resetaNumeroRespostasDistanciaBola();
}
