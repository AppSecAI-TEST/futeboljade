package view;

public interface CampoGraficoListener {

	void jogadorColidiuComBola(String nome);

	void jogadorEstaAXDistancia(String nome, double distancia);

	void jogadorEstaNaGrandeAreaAlvo(String nome);

	void bolaEstaNaGrandeAreaDoTime(String nome);

}
