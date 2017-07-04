package jogo.estilojogo;

import jogo.Jogador;

import java.util.stream.Stream;

public class EstiloAleatorio implements EstiloDeJogo {

	@Override
	public boolean deveChutar() {
		return chanceDeXPorcento(2);
	}

	@Override
	public int calculaErroDirecaoChute() {
		int random = MathUtil.randomEntre1eh100();
		if ((random % 2) == 0) {
			return (int) (Math.random() * 20) + 10;
		} else {
			return -(int) (Math.random() * 20) + 10;
		}
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
		return chanceDeXPorcento(4);
	}

	private boolean chanceDeXPorcento(int chance) {
		return MathUtil.randomEntre1eh100() < chance;
	}

	@Override
	public Jogador selecionaColegaPassarBola(Stream<Jogador> parceiros) {
		return parceiros.sorted((a, b) -> MathUtil.randomEntre1eh100() % 2 == 0 ? 1 : -1).findAny().orElse(Jogador.NULL);
	}

	@Override
	public void movimentaComBola(Jogador jogador) {
		jogador.atacar();
	}

	@Override
	public boolean deveCorrerAtrasDaBola(Jogador jogador) {
		return jogador.getDistanciaBola() < 150;
	}
}
