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
	@Getter
	@Setter
	private Time time;
	
	public Jogador() {
	}

	@Override
	protected void setup() {
		Object[] arguments = getArguments();
		this.nome = (String) arguments[0];
		this.time = new Time((String) arguments[1]);
		this.x = (int) arguments[2];
		this.y = (int) arguments[3];
		System.out.println(this.nome);
		addBehaviour(new JogarBehaviour());
	}

}
