package jogo.estilojogo;

import java.util.stream.Stream;

import jogo.Jogador;

public class EstiloAleatorio implements EstiloDeJogo {

	@Override
	public boolean deveChutar() {
		return chanceDeXPorcento(2);
	}

	@Override
	public int calculaErroDirecaoChute() {
		int random = randomEntre1eh100();
		if ((random % 2) == 0) {
			return (int) (Math.random() * 20) + 10;
		} else {
			return -(int) (Math.random() * 20) + 10;
		}
	}

	private int randomEntre1eh100() {
		return (int) (Math.random() * 100) + 1;
	}

	@Override
	public int calculaAceleracaoChute() {
		return (int) (Math.random() * 4) + 3;
	}

	@Override
	public int calculaVelocidadeChute() {
		return (int) (Math.random() * 4) + 3;
	}

	@Override
	public boolean devePassar() {
		return true; // chanceDeXPorcento(4);
	}

	private boolean chanceDeXPorcento(int porcentagem) {
		if (porcentagem == 0)
			porcentagem = Integer.MAX_VALUE;
		int fator = 100 / porcentagem;
		return randomEntre1eh100() % fator == 0;
	}

	@Override
	public Jogador selecionaColegaPassarBola(Stream<Jogador> parceiros) {
		return parceiros.sorted((a, b) -> randomEntre1eh100() % 2 == 0 ? 1 : -1).findAny().orElse(Jogador.NULL);
	}

}
