package jogo.estilojogo;

public class EstiloDeJogoFactory {

	public static EstiloDeJogo criaEstiloDeJogo() {
		return new EstiloAleatorio();
//		return new EstiloAleatorioFominha();
	}

	public static EstiloDeJogo criaEstiloDeJogoGoleiro() {
		return new EstiloDeJogoGoleiro();
	}

}
