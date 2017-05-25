package agent;

import agent.behaviour.JogarBehaviour;
import jade.core.Agent;
import lombok.Getter;
import lombok.Setter;

public class Jogador extends Agent {
	@Getter
	@Setter
	private int x, y;
	private String nome;

	public Jogador() {
	}

	@Override
	protected void setup() {
		Object[] arguments = getArguments();
		this.nome = (String) arguments[0];
		System.out.println(this.nome);
		addBehaviour(new JogarBehaviour());
	}

}
