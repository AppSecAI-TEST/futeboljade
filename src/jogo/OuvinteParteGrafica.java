package jogo;

import lombok.AllArgsConstructor;
import view.CampoGraficoListener;

import java.util.List;

@AllArgsConstructor
public class OuvinteParteGrafica implements CampoGraficoListener {

	private final jogo.Campo campo;
	
	@Override
	public void jogadorColidiuComBola(String nome) {
		campo.jogadorColidiuComBola(nome);
	}

	@Override
	public void jogadorEstaAXDistancia(String nome, double distancia) {
		campo.jogadorEstaAXDistancia(nome, distancia);
	}

	@Override
	public void bolaEstaNaGrandeAreaDoTime(String time) {
		campo.bolaEstaNaGrandeAreaDoTime(time);
	}

	@Override
	public void bolaSaiu() {
	}

	@Override
	public void jogadorEstaNaPosicao(String nome, Jogador.PosicaoCampo posicaoCampo) {
		campo.jogadorEstaNaPosicao(nome, posicaoCampo);
	}

	@Override
	public void jogadoresAFrente(String nome, List<String> nomes) {
		campo.jogadoresAFrente(nome, nomes);
	}

	@Override
	public void golTime(String nomeTime) {
		campo.golTime(nomeTime);
	}

	@Override
	public void bolaFoiProCentro() {
		campo.bolaFoiProCentro();
	}
}
