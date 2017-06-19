package jogo.estilojogo;

public class EstiloAleatorio implements EstiloDeJogo {

	@Override
	public boolean deveChutar() {
		return (int) (Math.random() * 100 % 25) == 0;
	}

	@Override
	public int calculaErroDirecaoChute() {
		int random = (int) (Math.random() * 100);
		if ( (random % 2) == 0){
			return (int) (Math.random() * 20) + 10;
		} else {
			return - (int) (Math.random() * 20) + 10;
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

}
