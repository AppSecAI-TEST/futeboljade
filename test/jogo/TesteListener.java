package jogo;

public class TesteListener extends CampoListenerAdapter {

	@Override
	public void jogadorIndoNaDirecaoDaBolaBemLoko(Jogador jogador) {
		System.out.printf("Jogador indo %s na diração da bola\n", jogador.getNome());
	}

	@Override
	public void jogadorAdicionado(Jogador jogador) {
		System.out.printf("Jogador %s adicionado\n");
	}
}