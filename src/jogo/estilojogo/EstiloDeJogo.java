package jogo.estilojogo;

import java.util.stream.Stream;

import jogo.Jogador;

public interface EstiloDeJogo {

	boolean deveChutar();

	int calculaErroDirecaoChute();

	int calculaAceleracaoChute();

	int calculaVelocidadeChute();

	boolean devePassar();

	Jogador selecionaColegaPassarBola(Stream<Jogador> stream);

}
