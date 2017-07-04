package grafico;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain=true)
public class Time {

	private String nome;
	private Set<Jogador> jogadores = new HashSet<>();
	private Color cor;
	private Gol golAlvo;
	private GrandeArea grandeAreaAlvo;
	public Time(Color cor) {
		this.cor = cor;
	}
	public Time(String nome) {
		this.nome = nome;
	}
	public void addJogador(Jogador jogador) {
		getJogadores().add(jogador);
		jogador.setTime(this);
	}
	
}
