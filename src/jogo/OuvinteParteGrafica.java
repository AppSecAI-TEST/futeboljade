package jogo;

import java.util.List;

import lombok.AllArgsConstructor;
import view.CampoGraficoListener;

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
	public void jogadorEstaNaGrandeAreaAlvo(String nome) {
		campo.jogadorEstaNaGrandeAreaAlvo(nome);
	}

	@Override
	public void bolaEstaNaGrandeAreaDoTime(String time) {
		campo.bolaEstaNaGrandeAreaDoTime(time);
	}

	@Override
	public void bolaNaoEstaNaGrandeAreaDoTime(String time) {
		campo.bolaNaoEstaNaGrandeAreaDoTime(time);
	}

	@Override
	public void bolaSaiu() {
		// TODO Auto-generated method stub
	}

	@Override
	public void jogadorEstaNoAtaque(String nome) {
		// TODO Auto-generated method stub
	}

	@Override
	public void jogadoresAFrente(List<String> nomes) {
		// TODO Auto-generated method stub
		
	}
}
