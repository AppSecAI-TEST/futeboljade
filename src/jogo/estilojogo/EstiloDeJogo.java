package jogo.estilojogo;

import jogo.Jogador;

import java.util.stream.Stream;

public interface EstiloDeJogo {

	boolean deveChutar();

	int calculaErroDirecaoChute();

	int calculaAceleracaoChute();

	int calculaVelocidadeChute();

	boolean devePassar();

	Jogador selecionaColegaPassarBola(Stream<Jogador> stream);

	void movimentaComBola(Jogador jogador);

    boolean deveCorrerAtrasDaBola(Jogador jogador);
}
