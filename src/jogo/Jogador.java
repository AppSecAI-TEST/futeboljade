package jogo;

import java.util.HashSet;
import java.util.Set;
import jade.core.Agent;
import jogo.behaviour.JogarBehaviour;
import lombok.Getter;
import lombok.Setter;

public class Jogador extends Agent {
	@Getter
	@Setter
	private String nome;
	@Getter
	private Time time = new Time("Sem time");

	@Getter
	@Setter
	private Campo campo;

	private Set<JogadorListener> listeners;

	@Override
	protected void setup() {
		Object[] arguments = getArguments();
		setNome((String) arguments[0]);
		setTime(new Time((String) arguments[1]));
		setCampo((Campo) arguments[2]);
		addBehaviour(new JogarBehaviour(this));
	}

	public Jogador() {
		this("Sem nome");
	}

	public Jogador(String nome) {
		this.nome = nome;
		listeners = new HashSet<>();
	}

	public void setTime(Time time) {
		time.addJogador(this);
		this.time = time;
	}

	public void correAtrasDaBola() {
		getCampo().mostraJogadorCorrendoAtrasDaBolaIgualUmTanso(this);
	}

	public void setColidiuComBola() {
		listeners.forEach(listener -> listener.colidiuComBola());
	}

	public void addListener(JogadorListener jogadorListener) {
		this.listeners.add(jogadorListener);
	}

}
