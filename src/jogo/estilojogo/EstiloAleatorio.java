package jogo.estilojogo;

import jogo.Jogador;
import lombok.Setter;

import java.util.List;

@Setter
public class EstiloAleatorio implements EstiloDeJogo {

	private Jogador.PosicaoCampo posicaoCampo = Jogador.PosicaoCampo.DEFESA;
	private int numeroRespostasDistanciaBola = 0;

	@Override
	public boolean deveChutar() {
		return chanceDeXPorcento( posicaoCampo.getChanceChutar() );
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
		return chanceDeXPorcento(posicaoCampo.getChancePassar());
	}

	private boolean chanceDeXPorcento(int chance) {
		return MathUtil.randomEntre1eh100() < chance;
	}

	@Override
	public Jogador selecionaColegaPassarBola(List<Jogador> parceiros) {
		if(numeroRespostasDistanciaBola < parceiros.size() )
			return Jogador.NULL;
		parceiros.stream().forEach(j->j.getDistanciaBola());
		Jogador jogador = parceiros.stream()
				.sorted((a, b) -> a.getDistanciaBola() > b.getDistanciaBola() ? 1 : -1)
				.limit(2).findAny().orElse(Jogador.NULL);
		return jogador;
	}

	@Override
	public void movimentaComBola(Jogador jogador) {
		jogador.atacar();
	}

	@Override
	public boolean deveCorrerAtrasDaBola(Jogador jogador) {
		return jogador.getDistanciaBola() < 150;
	}

	@Override
	public void incrementaNumeroRespostasDistanciaBola() {
		this.numeroRespostasDistanciaBola++;
	}

	@Override
	public void resetaNumeroRespostasDistanciaBola() {

	}
}
