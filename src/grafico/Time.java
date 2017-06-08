package grafico;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Time {

	private String nome;
	private Set<Jogador> jogadores = new HashSet<>();
	private Color cor;
	public Time(Color cor) {
		this.cor = cor;
	}
	public void addJogador(Jogador jogador) {
		getJogadores().add(jogador);
		jogador.setTime(this);
	}
	
}
