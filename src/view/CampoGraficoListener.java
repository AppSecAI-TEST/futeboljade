package view;

import java.util.List;

public interface CampoGraficoListener {

	void jogadorColidiuComBola(String nome);

	void jogadorEstaAXDistancia(String nome, double distancia);

	void jogadorEstaNaGrandeAreaAlvo(String nome);

	void bolaEstaNaGrandeAreaDoTime(String nome);

    void bolaNaoEstaNaGrandeAreaDoTime(String nome);
    
    void bolaSaiu();

	void jogadorEstaNoAtaque(String nome);

	void jogadoresAFrente(List<String> nomes);
}
