package view;

import jogo.Jogador;

import java.util.List;

public interface CampoGraficoListener {

	void jogadorColidiuComBola(String nome);

	void jogadorEstaAXDistancia(String nome, double distancia);

	void bolaEstaNaGrandeAreaDoTime(String nome);

    void bolaSaiu();

	void jogadorEstaNaPosicao(String nome, Jogador.PosicaoCampo posicaoCampo);

	void jogadoresAFrente(String nome, List<String> nomes);

    void golTime(String nome);

	void bolaFoiProCentro();
}
